package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import cs455.overlay.dijkstra.Vertex;
import cs455.overlay.node.MessagingNode;

public class Message implements Event
{
	private final int			_TYPE = Protocol.MESSAGE;
	private int 				_payload;
	private List<Vertex> 		_path;
	
	public Message()
	{
		_path = new LinkedList<Vertex>();
	}

	public Message(int payload, LinkedList<Vertex> path)
	{
		_payload = payload;
		_path = path;
	}

	public Message(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));

		int type = din.readInt(); //read int for type
		if(type != _TYPE) //invalid type
		{
			System.out.println("Invalid type");
			return;
		}

		_payload = din.readInt();
		
		int pathCount = din.readInt();
		for(int i = 0; i < pathCount; ++i)
		{
			int nodeLength = din.readInt();
			byte[] nodeBytes = new byte[nodeLength];
			din.readFully(nodeBytes);
			
			_path.add(new Vertex(nodeBytes));
		}

		baInputStream.close();
		din.close();
	}
	
	public void setPayload(int payload)
	{
		_payload = payload;
	}

	public int getPayload()
	{
		return _payload;
	}

	public void setPath(List<Vertex> path)
	{
		_path = path;
	}

	public List<Vertex> getPath()
	{
		return _path;
	}

	@Override
	public int getType()
	{
		return _TYPE;
	}

	@Override
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));

		dout.writeInt(_TYPE);
		
		dout.writeInt(_payload);
		
		dout.writeInt(_path.size());
		
		for(Vertex node : _path)
		{
			byte[] nodeBytes = node.getBytes();
			int elementLength = nodeBytes.length;
			dout.writeInt(elementLength);
			dout.write(nodeBytes);
		}

		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();

		baOutputStream.close();
		dout.close();

		return marshalledBytes;
	}
}
