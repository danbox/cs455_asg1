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
	
	public MessagingNode()
	{
		_server = new TCPServerThread(this);
		_connections = new Hashtable<String, Connection>();
	}
	
	public synchronized void on_event(Event event)
	{
		//TODO: registration, response, etc...
		switch(event.get_event_type())
		{
		case "message":
			Message message = (Message)event;
			String response = message.get_message().replaceAll("", "_");
			
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
		//TODO: figure out what the fuck to put here
		try
		{
			Socket socket = new Socket("localhost", 12321);
			MessagingNode node = new MessagingNode();
			Connection connection = new Connection(node, socket);
			node.registerConnection(connection);
			Scanner kb = new Scanner(System.in);
			String input = kb.nextLine();
			while(input != null || !input.equalsIgnoreCase("quit"))
			{
				connection.sendData(input.concat("\n").getBytes());
				String message = new String(receiver.receive());
				System.out.println("From server: " + message);
				input = kb.nextLine();
			}

		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}
}
