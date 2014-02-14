package cs455.overlay.node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;

import cs455.overlay.transport.*;
import cs455.overlay.wireformats.*;

public class MessagingNode implements Node 
{
	private TCPServerThread 				_server;
	private Hashtable<String, Connection> 	_connections;
    private String                          _registryHostName;
    private int                             _portnum;
    private String							_localHostAddress;
	
	public MessagingNode()
	{
		_server = new TCPServerThread(this);
		_connections = new Hashtable<String, Connection>();
		try
		{
			_localHostAddress = InetAddress.getLocalHost().getHostAddress();
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
				deregisterConnection(new Connection(this, socket));
			}
			System.out.println(registerResponse);
			break;
		case Protocol.DEREGISTER_RESPONSE:
			DeregisterResponse deregisterResponse = (DeregisterResponse)event;
			if(deregisterResponse.getSuccess() == 0)
			{
				deregisterConnection(new Connection(this, socket));
			}
			System.out.println(deregisterResponse);
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
	
	private void sendRegistrationRequest(Connection connection, Socket socket)
	{
		RegisterRequest request = new RegisterRequest(socket.getLocalAddress().toString(), socket.getLocalPort());
		try
		{
			connection.sendData(request.getBytes());
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	private void sendDeregistrationRequest(Connection connection, Socket socket)
	{
		DeregisterRequest request = new DeregisterRequest(socket.getLocalAddress().toString(), socket.getLocalPort());
		try
		{
			connection.sendData(request.getBytes());
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
        MessagingNode node = new MessagingNode();
        Socket socket = null;
        Connection connection = null;
        if(args.length != 2) //invalid number of arguments
        {
            System.out.println("Invalid arguments\nUsage: java cs455.overlay.node.MessagingNode <registry-host> <port-num>");
            System.exit(1);
        } else //parse command line args
        {
            node.setRegistryHostName(args[0]);
            try
            {
            	node.setPortNum(Integer.parseInt(args[1]));

            }catch(NumberFormatException nfe)
            {
            	nfe.printStackTrace();
            }
        }
		try
		{
			socket = new Socket(node.getRegistryHostName(), node.getPortNum());
			connection = new Connection(node, socket);
			node.sendRegistrationRequest(connection, socket);
			
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
				node.sendDeregistrationRequest(connection, socket);
				break;
			case "help":
				System.out.println("Help Menu:\n"
						+ "\tprint-shortest-path: prints shortest paths to all other messaging nodes within the system indicating the weights associated with the links\n"
						+ "\texit-overlay: allows messaging node to exit the overlay\n"
						+ "\thelp: prints help message\n"
						+ "\tquit: ends the messaging node");
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
