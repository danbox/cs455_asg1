package cs455.overlay.transport;

import cs455.overlay.node.*;

import java.io.IOException;
import java.net.Socket;

public class Connection {
	
	private String				_name;
	private String				_ip;
	private String				_localIP;
	private int					_port;
	private int					_localPort;
	private int					_listeningPort;
	private Node				_node;
	private TCPReceiverThread	_receiver; 
	private TCPSender			_sender;
	private int					_linkWeight;
	
	public Connection(Node node, Socket socket, int listeningPort) 
	{
		try
		{
			_ip = socket.getInetAddress().getCanonicalHostName();
			_localIP = socket.getLocalAddress().getCanonicalHostName();
			_port = socket.getPort();
			_localPort = socket.getLocalPort();
			_node = node;
			_listeningPort = listeningPort;
			_name = _ip + ":" + _listeningPort;
			_receiver = new TCPReceiverThread(node, socket); 
			_sender = new TCPSender(socket);
			_receiver.start(); //not sure where to put this?
			_linkWeight = -1; //-1 means that a weight has not been specified
			_node.registerConnection(this);
			System.out.println(_port + " " + _localPort + " " + _listeningPort);
		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public String getName()
	{
		return _name;
	}
	
	public Node getNode()
	{
		return _node;
	}
	
	public String getIP()
	{
		return _ip;
	}
	
	public String getLocalIP()
	{
		return _localIP;
	}
	
	public int getPort()
	{
		return _port;
	}
	
	public int getLocalPort()
	{
		return _localPort;
	}
	
	public int getListeningPort()
	{
		return _listeningPort;
	}
	
	public int getLinkWeight()
	{
		return _linkWeight;
	}
	
	public void setLinkWeight(int linkWeight)
	{
		_linkWeight = linkWeight;
	}
	
	public boolean sendData(byte[] bytes)
	{
		try
		{
			_sender.sendData(bytes);
		} catch(IOException ioe)
		{
			ioe.printStackTrace();
		} 
		return true;
	}

}
