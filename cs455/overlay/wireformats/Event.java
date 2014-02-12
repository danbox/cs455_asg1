package cs455.overlay.wireformats;

import java.io.IOException;

public interface Event 
{
	public Protocol getType();
	public byte[] getBytes() throws IOException;
}
