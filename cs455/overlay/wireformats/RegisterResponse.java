package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RegisterResponse implements Event
{
	private final int	_TYPE = Protocol.REGISTER_RESPONSE.value;
	private byte		_success;
	private String		_additionalInfo;
	
	public RegisterResponse()
	{
		_additionalInfo = new String();
	}
	
	public RegisterResponse(byte success, String additionalInfo)
	{
		_success = success;
		_additionalInfo = additionalInfo;
	}
	
	public RegisterResponse(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		
		din.readInt(); //read int for type
		
		_success = din.readByte();
		
		int infoLength = din.readInt();
		byte[] infoBytes = new byte[infoLength];
		din.readFully(infoBytes);
		
		_additionalInfo = new String(infoBytes);
		
		baInputStream.close();
		din.close();
	}
	
	@Override
	public Protocol getType() {
		return Protocol.REGISTER_RESPONSE;
	}

	@Override
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		
		dout.writeInt(_TYPE);
		
		dout.writeByte(_success);
		
		byte[] infoBytes = _additionalInfo.getBytes();
		int elementLength = infoBytes.length;
		dout.writeInt(elementLength);
		dout.write(infoBytes);
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		
		baOutputStream.close();
		dout.close();
		
		return marshalledBytes;
	}

}
