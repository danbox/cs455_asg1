package cs455.overlay.wireformats;

public class EventFactory 
{
	private static final EventFactory instance = new EventFactory();
	
	private EventFactory()
	{
		//TODO: ctor for factory
	}
	
	public static EventFactory get_instance()
	{
		return instance;
	}
	
	public static Event create_event(String event_type)
	{
		event_type = event_type.toLowerCase();
		
		switch(event_type)
		{
		case "message":
			return new Message();
		default:
			return new Message();
		}
	}
}
