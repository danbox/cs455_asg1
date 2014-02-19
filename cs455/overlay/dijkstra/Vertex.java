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
	
	public void setListeningPort(int port)
	{
		_listeningPort = port;
	}
	
	public int getListeningPort()
	{
		return _listeningPort;
	}
	
	@Override
	public boolean equals(Object vertex)
	{
//		System.out.println("EQUALS: " + _ip + ((Vertex) vertex).getIP() + _port + ((Vertex) vertex).getPort() + (_ip.equals(((Vertex) vertex).getIP()) && _port == ((Vertex) vertex).getPort()));
		return (_ip.equals(((Vertex) vertex).getIP()) && _port == ((Vertex) vertex).getPort());
	}
	
	@Override
	public String toString()
	{
		return _ip + ":" + _listeningPort;
	}
}
