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
    private String                          _registryIP
    private int                             _portnum
	
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

    public void setRegistryIP(String regIP)
    {
        _registryIP = regIP;
    }

    public String getRegistryIP()
    {
        return _registryIP;
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
        MessagingNode node = new MessagingNode();
        if(args.length != 3)
        {
            System.out.println("Invalid arguments\nUsage: java cs455.overlay.node.MessagingNode <registry-host> <port-num>");
            System.exit(1);
        } else
        {
            node.setRegistryIP(args[1]);
            node.setPortNum(Integer.parse(args[2]));
        }


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
			Socket socket = new Socket();
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
