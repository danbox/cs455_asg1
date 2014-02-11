package cs455.overlay.node;

import cs455.overlay.wireformats.Event;
import cs455.overlay.transport.*;

public interface Node 
{
	public void onEvent(Event event);
	
	public int getPortNum();
	
	public void registerConnection(Connection connection);
	
	public void deregisterConnection(Connection connection);
}
