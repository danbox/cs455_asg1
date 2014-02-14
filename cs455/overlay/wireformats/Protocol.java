package cs455.overlay.wireformats;

public class Protocol 
{
	//Event types
    public final static int	DEREGISTER_REQUEST      = 5000;
    public final static int DEREGISTER_RESPONSE		= 5001;
	public final static int LINK_WEIGHTS            = 5002;
	public final static int MESSAGE                 = 5003; 				
	public final static int REGISTER_REQUEST        = 5004;
	public final static int REGISTER_RESPONSE       = 5005;	
	public final static int TASK_COMPLETE           = 5006;
	public final static int TASK_INITIATE           = 5007;
	public final static int TASK_SUMMARY_REQUEST    = 5008;	
	public final static int TASK_SUMMARY_RESPONSE   = 5009;
	public final static int LINK_REQUEST			= 5010;
	public final static int LINK_RESPONSE			= 5011;
}
