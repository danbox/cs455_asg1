package cs455.overlay.wireformats;

public enum Protocol 
{
	//Event types
	DEREGISTER(0), 			
	LINKWEIGHTS(1), 			
	MESSAGE(2), 				
	REGISTER_REQUEST(3), 		
	REGISTER_RESPONSE(4), 		
	TASK_COMPLETE(5),
	TASK_INITIATE(6), 			
	TASK_SUMMARY_REQUEST(7),	
	TASK_SUMMARY_RESPONSE(8);

	public final int value;
	
	private Protocol(int value)
	{
		this.value = value;
	}
}
