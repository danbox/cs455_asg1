package cs455.overlay.transport;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cs455.overlay.node.*;

//TODO: used to accept communications

public class TCPServerThread extends Thread
{
//	private TCPReceiverThread	_receiver;
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
			ServerSocket ss = new ServerSocket(12321);

			while(true)
			{
				Socket socket = ss.accept();
				new Connection(_node, socket);
				
//				while(socket.isConnected())
//				{
//					_receiver = new TCPReceiverThread(socket);
//					_sender = new TCPSender(socket);
//					
//					byte[] raw_bytes = _receiver.receive();
//					String message = new String(raw_bytes);
//					message = message.replaceAll(" ", "_");
//					_sender.send_data(message.getBytes());
//				}
			}
		} catch(IOException ioe)
		{
			System.out.println(ioe.getMessage());
		}
	}
}
