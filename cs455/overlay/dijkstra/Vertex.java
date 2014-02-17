package cs455.overlay.dijkstra;

public class Vertex {

	private String	_ip;
	private int 	_port;
	
	public Vertex(String ip, int port)
	{
		_ip = ip;
		_port = port;
	}
	
	public String getIP()
	{
		return _ip;
	}
	
	public int getPort()
	{
		return _port;
	}
	
	@Override
	public String toString()
	{
		return _ip + ":" + _port;
	}
	
	public boolean equals(Vertex vertex)
	{
		return (_ip.equals(vertex.getIP()) && _port == vertex.getPort());
	}
}
