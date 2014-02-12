package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

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

	public static Event createEvent(byte[] data)
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(data);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));


		try
		{
			int eventType = din.readInt();
			switch(eventType)
			{
			case 0:
				return new Deregister();
			case 1:
				return new LinkWeights();
			case 2:
				return new Message();
			case 3:
				return new RegisterResponse();
			case 4:
				return new RegisterRequest(data);
			case 5:
				return new TaskComplete();
			case 6:
				return new TaskInitiate();
			case 7:
				return new TaskSummaryRequest();
			case 8:
				return new TaskSummaryResponse();
			default:
				return new Message();
			}
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		return null;
	}

	public static Event createEvent(Protocol eventType)
	{

		switch(eventType)
		{
		case REGISTER_REQUEST:
			return new RegisterRequest();
		default:
			return null;
		}

	}
}
