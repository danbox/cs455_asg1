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
	
	@Override
	public Protocol getType()
	{
		return null;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}
}
