package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;

import cs455.overlay.dijkstra.RoutingCache;
import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;

public class MessagingNode implements Node 
{
	private TCPServerThread 				_server;
	private Hashtable<String, Connection> 	_connections;
    private String                          _registryHostName;
    private int								_registryPortNum;
    private int                             _portnum;
    private String							_localHostAddress;
    private RoutingCache					_routingCache;
	
	public MessagingNode()
	{
		_server = new TCPServerThread(this);
		_connections = new Hashtable<String, Connection>();
		_routingCache = new RoutingCache();
		_portnum = 0;
		try
		{
			_localHostAddress = InetAddress.getLocalHost().getCanonicalHostName();
		}catch(UnknownHostException uhe)
		{
			uhe.printStackTrace();
		}
	}
	
	public String getLocalHostAddress()
	{
		return _localHostAddress;
	}
	
    public void setPortNum(int port)
    {
        _portnum = port;
    }
    
    public int getPortNum()
    {
        return _portnum;
    }

    public void setRegistryHostName(String regName)
    {
        _registryHostName = regName;
    }

    public String getRegistryHostName()
    {
        return _registryHostName;
    }
    
    public void setRegistryPortNum(int port)
    {
    	_registryPortNum = port;
    }
    
    public int getRegistryPortNum()
    {
    	return _registryPortNum;
    }
    
    public boolean equals(MessagingNode node)
    {
    	return _localHostAddress.equals(node.getLocalHostAddress());
    }

	public synchronized void onEvent(Event event, Socket socket)
	{
		//TODO: registration, response, etc...
		System.out.println("In Messaging Node onEvent(), event type: " + event.getType());
		switch(event.getType())
		{
		case Protocol.REGISTER_RESPONSE:
			RegisterResponse registerResponse = (RegisterResponse)event;
			if(registerResponse.getSuccess() != 0)
			{
//				deregisterConnection(new Connection(this, socket)); //TODO: fix new connection
			}
			System.out.println(registerResponse);
			break;
			
		case Protocol.DEREGISTER_RESPONSE:
			DeregisterResponse deregisterResponse = (DeregisterResponse)event;
			if(deregisterResponse.getSuccess() == 0)
			{
//				deregisterConnection(new Connection(this, socket)); //TODO: fix new connection
			}
			System.out.println(deregisterResponse);
			break;
			
		case Protocol.LINK_REQUEST:
			LinkRequest linkRequest = (LinkRequest)event;
			try
			{
				System.out.println(linkRequest.getIP() + linkRequest.getPort());
				Socket linkSocket = new Socket(linkRequest.getIP(), linkRequest.getPort());
				Connection linkConnection = new Connection(this, linkSocket);
				linkConnection.setLinkWeight(linkRequest.getLinkWeight());
				linkConnection.setListeningPort(_portnum);
				sendRegistrationRequest(linkConnection, linkConnection.getLinkWeight());
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			break;
			
		case Protocol.REGISTER_REQUEST:
			RegisterRequest registerRequest = (RegisterRequest)event;
			try
			{
				validateRegistration(registerRequest, socket);
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			
			break;
			
		case Protocol.LINK_WEIGHTS:
			System.out.println("Link Weights received");
			LinkWeights linkWeights = (LinkWeights)event;
			_routingCache.buildGraph(linkWeights);
			_routingCache.buildShortestPaths(this);
			break;
		
		default:
			System.out.println("Invalid event");	
		}
	}
	
	public synchronized void registerConnection(Connection connection)
	{
		_connections.put(connection.getName(), connection);
	}
	
	public synchronized void deregisterConnection(Connection connection)
	{
		_connections.remove(connection.getName());
	}
	
	private void sendRegistrationRequest(Connection connection, int linkWeight)
	{
		RegisterRequest request = new RegisterRequest(_localHostAddress, _portnum, linkWeight);
		try
		{
			connection.sendData(request.getBytes());
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	private void sendDeregistrationRequest(Connection connection)
	{
		DeregisterRequest request = new DeregisterRequest(_localHostAddress, _portnum);
		try
		{
			connection.sendData(request.getBytes());
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	private byte validateRegistration(RegisterRequest request, Socket socket) throws IOException
	{
		//success = 0, failure != 0
		byte success;
		String info = new String();
		Connection connection = _connections.get(socket.getInetAddress().getCanonicalHostName() + ":" + socket.getPort());
		
		connection.setLinkWeight(request.getLinkWeight());
		if(request.getIP().equals(socket.getInetAddress().getCanonicalHostName())) //valid ip address in request
		{
			success = 0;
			info = "Registration request successful";
			//			registerConnection(connection);

			//create register response

		} else
		{
			success = 1;
			deregisterConnection(connection);
			info = "Registration request unsuccessful.  The request IP " + request.getIP() + " does not match source IP " + socket.getInetAddress().getCanonicalHostName();
		}
//		RegisterResponse response = (RegisterResponse)EventFactory.createEvent(Protocol.REGISTER_RESPONSE);
		RegisterResponse response = new RegisterResponse();
		response.setSuccess(success);
		response.setAdditionalInfo(info);
		System.out.println("In messaging node: " + connection);
		connection.sendData(response.getBytes());

		return success;
	}
	
	public void listConnections()
	{
		Set<String> keys = _connections.keySet();
		for(String key : keys)
		{
			System.out.println(_connections.get(key));
		}
	}
	public static void main(String[] args)
	{
		//printing host name for debugging
		try
		{
			System.out.println("I am " + InetAddress.getLocalHost().getCanonicalHostName());
		}catch(UnknownHostException uhe)
		{
			uhe.printStackTrace();
		}
		
        MessagingNode node = new MessagingNode();
        node._server.start();
        Socket socket = null;
        Connection connection = null;
        
//        node.setPortNum(12322);
        if(args.length != 2) //invalid number of arguments
        {
            System.out.println("Invalid arguments\nUsage: java cs455.overlay.node.MessagingNode <registry-host> <port-num>");
            System.exit(1);
        } else //parse command line args
        {
            node.setRegistryHostName(args[0]);
            try
            {
            	node.setRegistryPortNum(Integer.parseInt(args[1]));

            }catch(NumberFormatException nfe)
            {
            	nfe.printStackTrace();
            }
        }
		try
		{
			//send registration request to registry
			System.out.println(node.getRegistryHostName() + node.getRegistryPortNum());
			socket = new Socket(node.getRegistryHostName(), node.getRegistryPortNum());
			connection = new Connection(node, socket);
			node.sendRegistrationRequest(connection, -1); //-1 defines no link weight
			
		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		
		
		Scanner in = new Scanner(System.in);
		boolean quit = false;
		while(!quit)
		{
			System.out.println("Awaiting input... Type help for help message");
			String command = in.nextLine();
			
			switch(command.toLowerCase())
			{
			case "print-shortest-path":
				System.out.println("Sorry this isn't implemented yet...");
				break;
				
			case "exit-overlay":
				System.out.println("Sorry this isn't implemented yet...");
				node.sendDeregistrationRequest(connection);
				break;
				
			case "help":
				System.out.println("Help Menu:\n"
						+ "\tprint-shortest-path: prints shortest paths to all other messaging nodes within the system indicating the weights associated with the links\n"
						+ "\texit-overlay: allows messaging node to exit the overlay\n"
						+ "\thelp: prints help message\n"
						+ "\tquit: ends the messaging node");
				break;
				
			case "list-connections":
				node.listConnections();
				break;
			case "print-cache":
				System.out.println(node._routingCache);
				break;
			case "quit":
				quit = true;
				break;
				
			default:
				System.out.println("Invalid command: " + command);	
			}
		}
		in.close();
	}
}
