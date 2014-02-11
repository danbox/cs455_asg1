package cs455.overlay.transport;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import cs455.overlay.node.*;
import cs455.overlay.wireformats.*;

public class TCPReceiverThread extends Thread
{
	private Socket				_socket;
	private Node				_node;
	private DataInputStream 	_din;
	private DataOutputStream	_dout; //not needed?
	private byte[]				_data;
	private int shit = 0;
	
	public TCPReceiverThread(Node node, Socket socket) throws IOException
	{
		_socket = socket;
		_node = node;
		_din = new DataInputStream(_socket.getInputStream());
		_dout = new DataOutputStream(_socket.getOutputStream()); //not needed?
	}
	
	public void run()
	{
		int data_length;

//		while(_socket != null)
//		{
			try
			{
//				data_length = _din.readInt();
				data_length = 10;
				
				_data = new byte[data_length];
				_din.readFully(_data, 0, data_length);
				
				System.out.println(new String(_data) + shit++);
				
				
//				Event event = EventFactory.create_event("message");
//				Message m = (Message)event;
//				m.set_message(new String(data));
//				_node.on_event(m);
				
			} catch(SocketException se)
			{
				se.printStackTrace();
//				break;
			} catch(IOException ioe)
			{
				ioe.printStackTrace();
//				break;
			}
//		}
	}
	
	public byte[] receiveData()
	{
		return _data;
	}
}
