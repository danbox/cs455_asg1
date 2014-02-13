package cs455.overlay.node;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
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
	
	@Override
	public synchronized void onEvent(Event event, Socket socket)
	{
		switch(event.getType())
		{
		case Protocol.REGISTER_REQUEST:
			RegisterRequest request = (RegisterRequest)event;
			//create connection?
			try
			{
				//Socket socket = new Socket(request.getIP(), request.getPort());
				//need to verify ip address...
				//otherwise send deregister?
				if(request.getIP().equals(socket.getInetAddress().toString())) //valid ip address in request
				{
					Connection connection = new Connection(this, socket);
					registerConnection(connection);
					
					//create register response
					Event responseEvent = EventFactory.createEvent(Protocol.REGISTER_RESPONSE);
					RegisterResponse response = (RegisterResponse)responseEvent;
					response.setSuccess((byte)0);
					response.setAdditionalInfo("");
					connection.sendData(response.getBytes());
				}
				
				
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
			
			
			break;
		default:
			//invalid event?
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
