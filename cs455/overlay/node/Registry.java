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
		_connections.put(connection.getName(), connection);
	}
	
	public synchronized void deregisterConnection(Connection connection)
	{
		_connections.remove(connection.getName());
	}
	
	public static void main(String[] args)
	{	
		Registry node = new Registry();
		
		//get port number
        if(args.length != 1)
        {
        	System.out.println(args.length);
            System.out.println("Invalid arguments\nUsage: java cs455.overlay.node.Registry <port-num>");
            System.exit(1);
        } else
        {
            try
            {
                node.setPortNum(Integer.parseInt(args[0]));
            } catch(NumberFormatException nfe)
            {
                nfe.printStackTrace();
            }
        }
		System.out.println("Server started");
		System.out.println(node.getPortNum());
		node._server.start();

	}
	
}
