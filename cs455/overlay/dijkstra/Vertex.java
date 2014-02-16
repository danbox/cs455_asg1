package cs455.overlay.dijkstra;

public class Vertex {

	private String	_name;
	
	public Vertex(String name)
	{
		_name = name;
	}
	
	public String getName()
	{
		return _name;
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
}
