package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TaskSummaryResponse implements Event
{

	public final int 	_TYPE = Protocol.TASK_SUMMARY_RESPONSE;
	private String 		_ip;
	private int			_port;
	private int 		_sendTracker;
	private long		_sendSummation;
	private int			_receiveTracker;
	private long		_receiveSummation;
	private int			_relayTracker;
	
	public TaskSummaryResponse()
	{
		_ip = new String();
	}
	
	public TaskSummaryResponse(String ip, int port, int sendTracker, long sendSummation, int receiveTracker, long receiveSummation, int relayTracker)
	{
		_ip = ip;
		_port = port;
		_sendTracker = sendTracker;
		_sendSummation = sendSummation;
		_receiveTracker = receiveTracker;
		_receiveSummation = receiveSummation;
		_relayTracker = relayTracker;
	}
	
	public TaskSummaryResponse(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		
		int type = din.readInt();
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
		
		_sendTracker = din.readInt();
		
		_sendSummation = din.readLong();
		
		_receiveTracker = din.readInt();
		
		_receiveSummation = din.readLong();
		
		_relayTracker = din.readInt();

		baInputStream.close();
		din.close();
	}
	
	@Override
	public int getType() 
	{
		return _TYPE;
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
	
	public int getSendTracker() 
	{
		return _sendTracker;
	}

	public void setSendTracker(int sendTracker) 
	{
		_sendTracker = sendTracker;
	}

	public long getSendSummation() 
	{
		return _sendSummation;
	}

	public void setSendSummation(long sendSummation) 
	{
		_sendSummation = sendSummation;
	}

	public int getReceiveTracker() 
	{
		return _receiveTracker;
	}

	public void setReceiveTracker(int receiveTracker)
	{
		_receiveTracker = receiveTracker;
	}

	public long getReceiveSummation() 
	{
		return _receiveSummation;
	}

	public void setReceiveSummation(long receiveSummation)
	{
		_receiveSummation = receiveSummation;
	}

	public int getRelayTracker() 
	{
		return _relayTracker;
	}

	public void setRelayTracker(int relayTracker) 
	{
		_relayTracker = relayTracker;
	}

	public int getPort()
	{
		return _port;
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
		
		dout.writeInt(_sendTracker);

		dout.writeLong(_sendSummation);
		
		dout.writeInt(_receiveTracker);
		
		dout.writeLong(_receiveSummation);
		
		dout.writeInt(_relayTracker);
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();

		baOutputStream.close();
		dout.close();

		return marshalledBytes;
	}

}
