package cs455.overlay.dijkstra;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Vertex {

	private String	_ip;
	private int 	_port;
	private int		_listeningPort;
	
	public Vertex(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}
	
	public Vertex(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		
		int ipLength = din.readInt();
		byte[] ipBytes = new byte[ipLength];
		din.readFully(ipBytes);
		_ip = new String(ipBytes);
		
		_port = din.readInt();
		
		_listeningPort = din.readInt();
		
		baInputStream.close();
		din.close();
	}
	
	public String getIP()
	{
		return _ip;
	}
	
	public int getPort()
	{
		return _port;
	}
	
	public void setListeningPort(int port)
	{
		_listeningPort = port;
	}
	
	public int getListeningPort()
	{
		return _listeningPort;
	}
	
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		
		byte[] ipBytes = _ip.getBytes();
		int elementLength = ipBytes.length;
		dout.writeInt(elementLength);
		dout.write(ipBytes);
		
		dout.writeInt(_port);
		
		dout.writeInt(_listeningPort);
	
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();

		baOutputStream.close();
		dout.close();

		return marshalledBytes;
	}
	
	@Override
	public boolean equals(Object vertex)
	{
//		System.out.println("EQUALS: " + _ip + ((Vertex) vertex).getIP() + _port + ((Vertex) vertex).getPort() + (_ip.equals(((Vertex) vertex).getIP()) && _port == ((Vertex) vertex).getPort()));
		return (_ip.equals(((Vertex) vertex).getIP()) && _port == ((Vertex) vertex).getPort());
	}
	
	@Override
	public String toString()
	{
		return _ip + ":" + _listeningPort;
	}
}
