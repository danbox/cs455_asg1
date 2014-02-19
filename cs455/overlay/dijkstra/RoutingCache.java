package cs455.overlay.dijkstra;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Node;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkWeights;

public class RoutingCache 
{

	private Graph 					_graph;
	Hashtable<Vertex, List<Vertex>> _shortestPaths;
	ShortestPath 					_shortestPath;
	
	public RoutingCache()
	{
		_graph = new Graph();
		_shortestPaths = new Hashtable<Vertex, List<Vertex>>();
		_shortestPath = new ShortestPath(_graph);
	}
	
	public void buildGraph(LinkWeights linkWeights)
	{
		for(LinkInfo linkInfo : linkWeights.getLinks())
		{
			Vertex source = new Vertex(linkInfo.get_sourceIP(), linkInfo.get_sourcePort());
			source.setListeningPort(linkInfo.get_sourceListeningPort());
			Vertex destination = new Vertex(linkInfo.get_destinationIP(), linkInfo.get_destinationPort());
			destination.setListeningPort(linkInfo.get_destinationListeningPort());
			
			if(!_graph.hasVertex(source))
			{
				_graph.addVertex(source);
				System.out.println(source + " added to cache");
			}
			Edge edge = new Edge(source, destination, linkInfo.get_weight());
			_graph.addEdge(edge);
		}
	}
	
	public List<Vertex> getNodes()
	{
		return _graph.getVertices();
	}
	
	public void buildShortestPaths(MessagingNode node)
	{
		System.out.println("Listening port: " + node.getPortNum());
		System.out.println(_graph.getSelf(node.getLocalHostAddress(), node.getPortNum()));
		System.out.println(node.getLocalHostAddress() + " " + node.getPortNum());
		_shortestPath.getShortestPaths(_graph.getSelf(node.getLocalHostAddress(), node.getPortNum())); //this is where null value is
		for(Vertex destination : _graph.getVertices())
		{
			_shortestPaths.put(destination, _shortestPath.getPath(destination));
		}
	}
	
	public LinkedList<Vertex> getPath(Vertex destination)
	{
		return _shortestPath.getPath(destination);
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Nodes: \n");
		for(Vertex node : _graph.getVertices())
		{
			stringBuilder.append(node + "\n");
		}
		stringBuilder.append("Edges: \n");
		stringBuilder.append(_graph);
		stringBuilder.append("Shortest Paths: \n");
		for(Vertex destination : _shortestPaths.keySet())
		{
			stringBuilder.append("To: " + destination + "\n");
			for(Vertex curr : _shortestPaths.get(destination))
			{
				stringBuilder.append(curr + "\n");
			}
		}
		return stringBuilder.toString();
	}
}
