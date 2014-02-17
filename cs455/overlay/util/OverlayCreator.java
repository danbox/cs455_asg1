package cs455.overlay.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cs455.overlay.dijkstra.*;
import cs455.overlay.transport.Connection;
import cs455.overlay.wireformats.LinkInfo;
import cs455.overlay.wireformats.LinkRequest;
import cs455.overlay.wireformats.LinkWeights;

public class OverlayCreator 
{

	public Graph		_graph;
	public final int	_NODE_PORT = 12322;

	public OverlayCreator()
	{
		_graph = new Graph();
	}

	//sends link requests to all connected nodes to create overlay with random link weights
	public void setupOverlay(Hashtable<String, Connection> connections)
	{
		//add all nodes to graph
		addNodesToGraph(connections);
		
		List<Connection> connList = new ArrayList<Connection>(connections.values());

		//first iteration
		for(int i = 0; i < connList.size(); ++ i)
		{
			int destinationIndex;
			if(i == connList.size() - 1) //if this is the last node
			{
				destinationIndex = 0; //sets to first node
			}else
			{
				destinationIndex = i + 1;
			}

			//create request and specify random link weight
			int linkWeight = (int)(1 + Math.random() * (10));
			LinkRequest linkRequest = new LinkRequest(connList.get(destinationIndex).getIP(), _NODE_PORT, linkWeight);

			//add edge to graph
			Vertex source = _graph.getVertex(connList.get(i).getIP(), connList.get(i).getPort());
			Vertex destination = _graph.getVertex(connList.get(destinationIndex).getIP(), connList.get(destinationIndex).getPort());
			_graph.addEdge(new Edge(source, destination, linkWeight));
			
			//send data
			try
			{
				connList.get(i).sendData(linkRequest.getBytes());
				TimeUnit.MILLISECONDS.sleep(20);
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}catch(InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}

		//second iteration
		for(int i = 0; i < connList.size(); ++ i)
		{
			int destinationIndex;
			if(i == connList.size() - 2) //if this is the second to last node
			{
				destinationIndex = 0;
			}else if(i == connList.size() - 1) //if this is the last node
			{
				destinationIndex = 1; //sets to first node
			}else
			{
				destinationIndex = i + 2;
			}

			//create request and specify random link weight
			int linkWeight = (int)(1 + Math.random() * (10));
			LinkRequest linkRequest = new LinkRequest(connList.get(destinationIndex).getIP(), _NODE_PORT, linkWeight);

			//add edge to graph
			Vertex source = _graph.getVertex(connList.get(i).getIP(), connList.get(i).getPort());
			Vertex destination = _graph.getVertex(connList.get(destinationIndex).getIP(), connList.get(destinationIndex).getPort());
			_graph.addEdge(new Edge(source, destination, linkWeight));
			
			//send data
			try
			{
				connList.get(i).sendData(linkRequest.getBytes());
			}catch(IOException ioe)
			{
				ioe.printStackTrace();
			}

		}
	}
	
	public void addNodesToGraph(Hashtable<String, Connection> connections)
	{
		List<Connection> connList = new ArrayList<Connection>(connections.values());
		
		for(Connection connection : connList)
		{
			_graph.addVertex(new Vertex(connection.getIP(), connection.getPort()));
		}
	}
	
	public LinkWeights generateLinkWeightMessage()
	{
		List<LinkInfo> infoList = new ArrayList<LinkInfo>();
		for(Edge edge : _graph.getEdges())
		{
			Vertex source = edge.getSource();
			Vertex destination = edge.getDestination();
			infoList.add(new LinkInfo(source.getIP(), source.getPort(), destination.getIP(), destination.getPort(), edge.getWeight()));
		}
		LinkWeights linkWeights = new LinkWeights(infoList.size(), infoList);
		System.out.println(linkWeights);
		return linkWeights;
	}
	
	@Override
	public String toString()
	{
		return _graph.toString();
	}
}
