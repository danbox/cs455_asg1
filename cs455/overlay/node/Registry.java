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
			RegisterRequest registerRequest = (RegisterRequest)event;
			//create connection?
			try
			{
				//Socket socket = new Socket(request.getIP(), request.getPort());
				//need to verify ip address...
				//otherwise send deregister?
				validateRegistration(registerRequest, socket);


			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}


			break;
		case Protocol.DEREGISTER_REQUEST:
			DeregisterRequest deregisterRequest = (DeregisterRequest)event;
			try
			{
				//Socket socket = new Socket(request.getIP(), request.getPort());
				//need to verify ip address...
				//otherwise send deregister?
				validateDeregistration(deregisterRequest, socket);


			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}
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

	private byte validateRegistration(RegisterRequest request, Socket socket) throws IOException
	{
		//success = 0, failure != 0
		byte success;
		String info = new String();
		Connection connection = _connections.get(socket.getLocalAddress().toString() + ":" + socket.getPort());
		if(request.getIP().equals(socket.getInetAddress().toString())) //valid ip address in request
		{
			success = 0;
			info = "Registration request successful.  The number of messaging nodes currently constituting the overlay is: " + _connections.size();
			//			registerConnection(connection);

			//create register response

		} else
		{
			success = 1;
			deregisterConnection(connection);
			info = "Registration request unsuccessful.  The request IP " + request.getIP() + " does not match source IP " + socket.getInetAddress().toString();
		}
//		RegisterResponse response = (RegisterResponse)EventFactory.createEvent(Protocol.REGISTER_RESPONSE);
		RegisterResponse response = new RegisterResponse();
		response.setSuccess(success);
		response.setAdditionalInfo(info);
		connection.sendData(response.getBytes());

		return success;
	}

	private byte validateDeregistration(DeregisterRequest request, Socket socket) throws IOException
	{
		//success = 0, failure != 0
		byte success;
		String info = new String();
		Connection connection = _connections.get(socket.getLocalAddress().toString() + ":" + socket.getPort());
		if(request.getNodeIP().equals(socket.getInetAddress().toString())) //valid ip address in request
		{
			success = 0;
			deregisterConnection(connection);
			info = "Deregistration request successful.  The number of messaging nodes currently constituting the overlay is: " + _connections.size();

		} else
		{
			success = 1;
			info = "Deregistration request unsuccessful.  The request IP " + request.getNodeIP() + " does not match source IP " + socket.getInetAddress().toString();
		}
//		DeregisterResponse response = (DeregisterResponse)EventFactory.createEvent(Protocol.DEREGISTER_RESPONSE);
		DeregisterResponse response = new DeregisterResponse();
		response.setSuccess(success);
		response.setAdditionalInfo(info);
		connection.sendData(response.getBytes());

		return success;
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
