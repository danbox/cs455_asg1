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
	
	public synchronized void onEvent(Event event)
	{
		//TODO: registration, response, etc...
		switch(event.get_event_type())
		{
		case "message":
			Message message = (Message)event;
			String response = message.get_message().replaceAll("", "_");
		default:
			System.out.println("fdjskafds");	
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
		Scanner keyboard = new Scanner(System.in);
//		System.out.println("Send or Receive?");
//		String input = keyboard.nextLine();
//		switch(input.charAt(0))
//		{
//		case 's':
//			//do send
//			break;
//		case 'r':
//			//do receive
//			break;
//		default:
//			System.out.println("invalid");
//			break;
//		}
		try
		{
			MessagingNode node = new MessagingNode();
			Socket socket = new Socket("localhost", 12323);
//			MessagingNode node = new MessagingNode();
//			Connection connection = new Connection(node, socket);
//			node.registerConnection(connection);
			Scanner kb = new Scanner(System.in);
			String input = kb.nextLine();
			while(input != null || !input.equalsIgnoreCase("quit"))
			{
				TCPSender sender = new TCPSender(socket);
				sender.sendData(input.concat("\n").getBytes());
				
			}

		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
	}
}
