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
        
//        System.out.println(data);

		try
		{
			int eventType = din.readInt();
	        baInputStream.close();
	        din.close();
	        
	        System.out.println("Inside event factory, event type: " + eventType);
	        
			switch(eventType)
			{
			case Protocol.DEREGISTER_REQUEST:
				System.out.println("Creating deregister request");
				return new DeregisterRequest(data);
			case Protocol.DEREGISTER_RESPONSE:
				System.out.println("Creating deregister response");
				return new DeregisterResponse(data);
			case Protocol.LINK_WEIGHTS:
				return new LinkWeights();
			case Protocol.MESSAGE:
				return new Message();
			case Protocol.REGISTER_RESPONSE:
				System.out.println("Creating register response");
				return new RegisterResponse(data);
			case Protocol.REGISTER_REQUEST:
				System.out.println("Creating register request");
				return new RegisterRequest(data);
			case Protocol.TASK_COMPLETE:
				return new TaskComplete();
			case Protocol.TASK_INITIATE:
				return new TaskInitiate();
			case Protocol.TASK_SUMMARY_REQUEST:
				return new TaskSummaryRequest();
			case Protocol.TASK_SUMMARY_RESPONSE:
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

	public static Event createEvent(int eventType)
	{

		switch(eventType)
		{
		case Protocol.REGISTER_REQUEST:
			return new RegisterRequest();
		case Protocol.REGISTER_RESPONSE:
			return new RegisterResponse();
		default:
			return null;
		}

	}
}
