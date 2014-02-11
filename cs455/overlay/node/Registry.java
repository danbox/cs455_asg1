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
    private int                             _portnum;
	
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

    public void setPortNum(int port)
    {
        _portnum = port;
    }

    public int getPortNum()
    {
        return _portnum;
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
		//get port number
        if(args.length != 2)
        {
            System.out.println("Invalid arguments\nUsage: java cs455.overlay.node.Registry <port-num>");
            System.exit(1);
        } else
        {
            try
            {
                _portnum = Integer.parseInt(args[1]);
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }
		System.out.println("Server started");
		
		Registry node = new Registry();
		while(true){}

	}
	
}
