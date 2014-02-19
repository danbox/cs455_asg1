package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import cs455.overlay.dijkstra.RoutingCache;
import cs455.overlay.dijkstra.Vertex;
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
				linkConnection.setListeningPort(linkRequest.getPort());
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

		case Protocol.MESSAGE:
			Message message = (Message)event;
			System.out.println("Message received");

			LinkedList<Vertex> path = message.getPath();

			System.out.println(path);
			path.poll();

			System.out.println(path);

			if(path.isEmpty()) //reached destination
			{
				System.out.println("Reached desination!");
			}else
			{
				//get next node in path
				Vertex next = path.element();

				//get connection
				Connection conn = _connections.get(next.getIP() + ":" + next.getListeningPort());


				//create message
				int payload = (int)Math.random();
				Message nextMessage = new Message(payload, path);
				//				System.out.println(next.getIP() + ":" + next.getPort() + ":" + next.getListeningPort());
				System.out.println(conn);

				//send message
				try
				{
					conn.sendData(nextMessage.getBytes());
				}catch(IOException ioe)
				{
					ioe.printStackTrace();
				}

			}
			break;

		case Protocol.TASK_INITIATE:
			sendMessage();
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
		connection.setListeningPort(request.getPort());

		//replace with correct port
		_connections.remove(socket.getInetAddress().getCanonicalHostName() + ":" + socket.getPort());
		_connections.put(socket.getInetAddress().getCanonicalHostName() + ":" + request.getPort(), connection);

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
			System.out.println(_connections.get(key).getName());
			System.out.println(_connections.get(key).getLocalPort());
			System.out.println(_connections.get(key).getListeningPort());
		}
	}

	private void sendMessage()
	{
		//get random target
		List<Vertex> nodes = _routingCache.getNodes();
		int targetIndex = (int)(1 + Math.random() * (nodes.size() - 1));
		Vertex target = nodes.get(targetIndex);

		//set path
		LinkedList<Vertex> path = _routingCache.getPath(target);

		while(path.size() == 1) //chose self
		{
			targetIndex = (int)(1 + Math.random() * (nodes.size() - 1));
			target = nodes.get(targetIndex);
			path = _routingCache.getPath(target);
		}

		//remove self from path
		path.poll();
		System.out.println("Target: " + target);
		System.out.println("Destination: " + path.getLast());
		//get next node in path
		Vertex next = path.element();
		System.out.println("Next: " + next);

		System.out.println(path);

		//get connection
		System.out.println(next.getIP() + ":" + next.getPort() + ":" + next.getListeningPort());
		Connection conn = _connections.get(next.getIP() + ":" + next.getListeningPort());
		if(conn == null)
		{
			System.out.println("it was null.........");
			conn = _connections.get(next.getIP() + ":" + next.getPort());
		}
		System.out.println(conn);

		//send 5 messages per rounds
		for(int i = 0; i < 5; ++i)
		{
			int payload = (int)Math.random();
			Message message = new Message(payload, path);
			try
			{
				conn.sendData(message.getBytes());
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
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
			case "send-message":
				node.sendMessage();
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
