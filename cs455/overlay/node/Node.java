package cs455.overlay.node;

import cs455.overlay.wireformats.Event;
import cs455.overlay.transport.*;
import java.net.Socket;

public interface Node 
{
	public void onEvent(Event event, Socket socket);
	
	public int getPortNum();
	
	public void setPortNum(int port);
	
	public void registerConnection(Connection connection);
	
	public void deregisterConnection(Connection connection);
}
