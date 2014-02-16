package cs455.overlay.dijkstra;

public class Edge {

	private Vertex	_source;
	private Vertex	_destination;
	private int		_weight;
	
	public Edge(Vertex source, Vertex destination, int weight)
	{
		_source = source;
		_destination = destination;
		_weight = weight;
	}
	
	public Vertex getSource()
	{
		return _source;
	}
	
	public Vertex getDestination()
	{
		return _destination;
	}
	
	public int getWeight()
	{
		return _weight;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Source: ");
		stringBuilder.append(_source);
		stringBuilder.append(" Destination: ");
		stringBuilder.append(_destination);
		stringBuilder.append(" Weight: ");
		stringBuilder.append(_weight);
		return stringBuilder.toString();
	}
}
