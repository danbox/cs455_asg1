package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DeregisterRequest implements Event
{
	private final int 	_TYPE = Protocol.DEREGISTER_REQUEST;
	private String		_nodeIP;
	private int			_nodePort;

	public DeregisterRequest()
	{
		_nodeIP = new String();
	}
	
	public DeregisterRequest(String ip, int port)
	{
		_nodeIP = ip;
		_nodePort = port;
	}
	
	public String getIP()
	{
		return _nodeIP;
	}
	
	public int getPort()
	{
		return _nodePort;
	}
	
	public DeregisterRequest(byte[] marshalledBytes) throws IOException
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
		
		_nodeIP = new String(ipBytes);
		
		_nodePort = din.readInt();
		
		baInputStream.close();
		din.close();
	}
	
	public void setNodeIP(String ip)
	{
		_nodeIP = ip;
	}
	
	public String getNodeIP()
	{
		return _nodeIP;
	}
	
	public void setNodePort(int port)
	{
		_nodePort = port;
	}
	
	public int getNodePort()
	{
		return _nodePort;
	}
	@Override
	public int getType() {
		return _TYPE;
	}

	@Override
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		
		dout.writeInt(_TYPE);
		
		byte[] ipBytes = _nodeIP.getBytes();
		int elementLength = ipBytes.length;
		dout.writeInt(elementLength);
		dout.write(ipBytes);
		
		dout.writeInt(_nodePort);
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		
		baOutputStream.close();
		dout.close();
		
		return marshalledBytes;
	}

}
