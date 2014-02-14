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
	
	public TCPReceiverThread(Node node, Socket socket) throws IOException
	{
		_socket = socket;
		_node = node;
		_din = new DataInputStream(_socket.getInputStream());
		_dout = new DataOutputStream(_socket.getOutputStream()); //not needed?
	}
	
	public void run()
	{
		int dataLength;

		while(_socket != null)
		{
			try
			{
				dataLength = _din.readInt();
				System.out.println("I am receiver thread: " + Thread.currentThread().getId());
				byte[] data = new byte[dataLength];
				System.out.println("inside receiver thread Data length: " + dataLength);
				
				_din.readFully(data, 0, dataLength);
//						System.out.println(new String(_data));
				System.out.println("after readFully in receiver thread");
				Event event = EventFactory.createEvent(data);
				_node.onEvent(event, _socket);
				
//				Event event = EventFactory.create_event("message");
//				Message m = (Message)event;
//				m.set_message(new String(data));
//				_node.on_event(m);
				
			} catch(SocketException se)
			{
				se.printStackTrace();
				break;
			} catch(IOException ioe)
			{
				ioe.printStackTrace();
				break;
			}
		}

	}
}
