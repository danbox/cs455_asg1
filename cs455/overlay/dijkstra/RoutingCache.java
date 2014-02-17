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
				System.out.println(source + " added to cache");
			}
			if(!_graph.hasVertex(destination))
			{
				_graph.addVertex(destination);
				System.out.println(destination + " added to cache");
			}
			Edge edge = new Edge(source, destination, linkInfo.get_weight());
			_graph.addEdge(edge);
			System.out.println(edge + " added to cache");
		}
	}
	
	@Override
	public String toString()
	{
		return _graph.toString();
	}
}
