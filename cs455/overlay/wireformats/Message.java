package cs455.overlay.wireformats;

public class Message implements Event
{
	private String _message;
	
	public Message()
	{
		_message = new String();
	}
	
	public Message(String message)
	{
		_message = message;
	}
	
	public void set_message(String message)
	{
		_message = message;
	}
	
	public String get_message()
	{
		return _message;
	}
	
	public String get_event_type()
	{
		return "message";
	}
}
