package cs455.overlay.transport;

import cs455.overlay.node.*;

import java.io.IOException;
import java.net.Socket;

public class Connection {
	
	private String				_name;
	private Node				_node;
	private TCPReceiverThread	_receiver; 
	private TCPSender			_sender;
	
	public Connection(Node node, Socket socket) 
	{
		try
		{
			_name = socket.getLocalAddress().toString() + ":" + socket.getPort();
			_node = node;
			_receiver = new TCPReceiverThread(node, socket); 
			_sender = new TCPSender(socket);
			
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
