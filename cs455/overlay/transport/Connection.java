package cs455.overlay.transport;

import cs455.overlay.node.*;

import java.io.IOException;
import java.net.Socket;

public class Connection {
	
	private String				_name;
	private String				_ip;
	private int					_port;
	private Node				_node;
	private TCPReceiverThread	_receiver; 
	private TCPSender			_sender;
	private int					_linkWeight;
	
	public Connection(Node node, Socket socket) 
	{
		try
		{
			_name = socket.getInetAddress().getCanonicalHostName() + " : " + socket.getPort();
			_ip = socket.getInetAddress().getCanonicalHostName();
			_port = socket.getPort();
			_node = node;
			_receiver = new TCPReceiverThread(node, socket); 
			_sender = new TCPSender(socket);
			_receiver.start(); //not sure where to put this?
			_linkWeight = -1; //-1 means that a weight has not been specified
			_node.registerConnection(this);
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
	
	public int getPort()
	{
		return _port;
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
