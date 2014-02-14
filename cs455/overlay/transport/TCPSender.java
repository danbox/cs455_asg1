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
	
	public void sendData(byte[] dataToSend) throws IOException
	{
		int data_length = dataToSend.length;
		_dout.writeInt(data_length);
		_dout.write(dataToSend, 0, data_length);
		_dout.flush();
	}
}
