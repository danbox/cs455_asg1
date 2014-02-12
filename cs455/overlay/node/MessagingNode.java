package cs455.overlay.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	public MessagingNode()
	{
		_server = new TCPServerThread(this);
		_connections = new Hashtable<String, Connection>();
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

	public synchronized void onEvent(Event event)
	{
		//TODO: registration, response, etc...
		switch(event.getType())
		{
		case REGISTER_RESPONSE:
			
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
	
	
	public static void main(String[] args)
	{
        MessagingNode node = new MessagingNode();

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
			Socket socket = new Socket(node.getRegistryHostName(), node.getPortNum());
			Connection connection = new Connection(node, socket);
			RegisterRequest event = (RegisterRequest)EventFactory.createEvent(Protocol.REGISTER_REQUEST);
			event.setIP(socket.getLocalAddress().toString());
			event.setPort(socket.getLocalPort());
			connection.sendData(event.getBytes());

		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}
}
