package cs455.overlay.dijkstra;

public class Vertex {

	private String	_ip;
	private int 	_port;
	private int		_listeningPort;
	
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
	
	public int getListeningPort()
	{
		return _listeningPort;
	}
	
	@Override
	public String toString()
	{
		return _ip + ":" + _listeningPort;
	}
	
	public boolean equals(Vertex vertex)
	{
		return (_ip.equals(vertex.getIP()) && _port == vertex.getPort());
	}
}
