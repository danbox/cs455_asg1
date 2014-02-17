package cs455.overlay.dijkstra;

import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkWeights;

public class RoutingCache 
{

	private Graph _graph;
	
	public RoutingCache()
	{
		_graph = new Graph();
	}
	
	public void buildCache(Event event)
	{
		LinkWeights linkWeights = (LinkWeights)event;
		for(LinkInfo linkInfo : linkWeights.getLinks())
		{
			Vertex source = new Vertex(linkInfo.get_sourceIP(), linkInfo.get_sourcePort());
			Vertex destination = new Vertex(linkInfo.get_destinationIP(), linkInfo.get_destinationPort());
			
			if(!_graph.hasVertex(source))
			{
				_graph.addVertex(source);
			}
			if(!_graph.hasVertex(destination))
			{
				_graph.addVertex(destination);
			}
			_graph.addEdge(new Edge(source, destination, linkInfo.get_weight()));
		}
	}
	
	@Override
	public String toString()
	{
		return _graph.toString();
	}
}
