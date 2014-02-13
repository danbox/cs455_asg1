package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterRequest implements Event
{
	private final int 	_TYPE = Protocol.REGISTER_REQUEST;
	private String 		_ip;
	private int			_port;
	
	public RegisterRequest()
	{
		_ip = new String();
	}
	
	public RegisterRequest(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}
	
	public RegisterRequest(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		
		int type = din.readInt(); //read int for type
        if(type != _TYPE) //invalid type
        {
            System.out.println("Invalid type");
            return;
        }
		
		int ipLength = din.readInt();
		byte[] ipBytes = new byte[ipLength];
		din.readFully(ipBytes);
		
		_ip = new String(ipBytes);
		
		_port = din.readInt();
		
		baInputStream.close();
		din.close();
	}
	
	public void setIP(String ip)
	{
		_ip = ip;
	}
	
	public String getIP()
	{
		return _ip;
	}
	
	public void setPort(int port)
	{
		_port = port;
	}
	
	public int getPort()
	{
		return _port;
	}
	
	@Override
	public int getType() {
		return Protocol.REGISTER_REQUEST;
	}

	@Override
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		
		dout.writeInt(_TYPE);
		
		byte[] ipBytes = _ip.getBytes();
		int elementLength = ipBytes.length;
		dout.writeInt(elementLength);
		dout.write(ipBytes);
		
		dout.writeInt(_port);
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		
		baOutputStream.close();
		dout.close();
		
		return marshalledBytes;
	}

    @Override
    public String toString()
    {
        return "IP address: " + _ip + ", Port number: " + _port;
    }

}
