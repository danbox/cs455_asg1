package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.*;

//TODO: used to accept communications

public class TCPServerThread extends Thread
{
	private TCPReceiverThread	_receiver;
//	private TCPSender			_sender;
	private Node				_node;
	
	
	public TCPServerThread(Node node)
	{
		_node = node;
		this.start();
	}

	public void run()
	{
		try
		{
			ServerSocket ss = new ServerSocket(12321); //how to close?

			while(true)
			{
				Socket socket = ss.accept();
				new Connection(_node, socket);
				new TCPReceiverThread(_node, socket);
			}
			
		} catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
		
	}
}
