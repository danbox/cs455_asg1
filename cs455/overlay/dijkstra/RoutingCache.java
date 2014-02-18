package cs455.overlay.dijkstra;

import java.util.Hashtable;
import java.util.List;

import cs455.overlay.node.MessagingNode;
import cs455.overlay.node.Node;
import cs455.overlay.wireformats.Event;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkWeights;

public class RoutingCache 
{

	private Graph 					_graph;
	Hashtable<String, List<Vertex>> _shortestPaths;
	
	public RoutingCache()
	{
		_graph = new Graph();
		_shortestPaths = new Hashtable<String, List<Vertex>>();
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
	
	public void buildShortestPaths(MessagingNode node)
	{
		ShortestPath shortestPath = new ShortestPath(_graph);
		System.out.println("Listening port: " + node.getPortNum());
		System.out.println(_graph.getSelf(node.getLocalHostAddress(), node.getPortNum()));
		System.out.println(node.getLocalHostAddress() + " " + node.getPortNum());
		shortestPath.getShortestPaths(_graph.getSelf(node.getLocalHostAddress(), node.getPortNum())); //this is where null value is
		for(Vertex destination : _graph.getVertices())
		{
			_shortestPaths.put(destination.toString(), shortestPath.getPath(destination));
		}
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
		for(String destination : _shortestPaths.keySet())
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
