package cs455.overlay.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;

import cs455.overlay.wireformats.*;
import cs455.overlay.transport.*;

public class Registry implements Node
{
	private TCPServerThread 				_server;
	private Hashtable<String, Connection> 	_connections;
	
	public Registry()
	{
		_server = new TCPServerThread(this);
		_connections = new Hashtable<String, Connection>();
	}
	
	public synchronized void onEvent(Event event)
	{
		switch(event.get_event_type())
		{
		case "message":
			break;
		default:
			break;
		}
	}
	
	public synchronized void registerConnection(Connection connection)
	{
		
	}
	
	public synchronized void deregisterConnection(Connection connection)
	{
		
	}
	
	public static void main(String[] args)
	{
//		try
//		{
//			ServerSocket serverSocket = new ServerSocket(12321);
//			
//		} catch(IOException ioe)
//		{
//			ioe.printStackTrace();
//		}
		
		System.out.println("Server started");
		
		Registry node = new Registry();
		while(true){}

	}
	
}
