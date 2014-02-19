package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.*;

//TODO: used to accept communications

public class TCPServerThread extends Thread
{
//	private TCPReceiverThread	_receiver; //don't think is needed
//	private TCPSender			_sender;
	private Node				_node;
	
	
	public TCPServerThread(Node node)
	{
		_node = node;
	}

	public void run()
	{
		try
		{
			ServerSocket ss = new ServerSocket(_node.getPortNum()); //how to close?
			_node.setPortNum(ss.getLocalPort());
			while(true)
			{
//				System.out.println("Inside Server Thread loop");
				Socket socket = ss.accept();
				new Connection(_node, socket);
				
				
				
				
//				new TCPReceiverThread(_node, socket);
//				byte[] raw = conn.recieveData();
//				String message = new String(raw);
//				message = message.replaceAll("",  "_");
//				conn.sendData(message.getBytes());
			}
			
		} catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
	}
}
