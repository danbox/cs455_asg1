package cs455.overlay.wireformats;

public class Protocol 
{
	//Event types
    public final static int	DEREGISTER              = 5000;
	public final static int LINK_WEIGHTS            = 5001;
	public final static int MESSAGE                 = 5002, 				
	public final static int REGISTER_REQUEST        = 5003;
	public final static int REGISTER_RESPONSE       = 5004;	
	public final static int TASK_COMPLETE           = 5005;
	public final static int TASK_INITIATE           = 5006;
	public final static int TASK_SUMMARY_REQUEST    = 5007;	
	public final static int TASK_SUMMARY_RESPONSE   = 5008;

}
