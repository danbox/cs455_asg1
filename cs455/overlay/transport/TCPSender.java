package cs455.overlay.transport;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSender 
{
	private Socket				_socket; //not needed?
	private DataOutputStream 	_dout;
	
	public TCPSender(Socket socket) throws IOException
	{
		_socket = socket;
		_dout = new DataOutputStream(socket.getOutputStream());
	}
	
	public void sendData(byte[] data_to_send) throws IOException
	{
		int data_length = data_to_send.length;
//		data_length = 10;
		_dout.writeInt(data_length);
		_dout.write(data_to_send, 0, data_length);
		_dout.flush();
	}
}
