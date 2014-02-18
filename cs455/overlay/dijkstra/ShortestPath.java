package cs455.overlay.dijkstra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Iterator;

public class ShortestPath 
{
	private List<Vertex> 		_nodes;
	private List<Edge> 			_edges;
	Hashtable<Vertex, Integer> 	_distances;
	Hashtable<Vertex, Vertex> 	_predecessors;
	List<Vertex> 				_unoptimizedNodes;
	
	public ShortestPath(Graph graph)
	{
		_nodes = new ArrayList<Vertex>(graph.getVertices());
		_edges = new ArrayList<Edge>(graph.getEdges());
	}
	
	private List<Vertex> getNeighbors(Vertex node)
	{
		List<Vertex> neighbors = new ArrayList<Vertex>();
		
		for(Edge edge : _edges)
		{
			if(edge.getSource().equals(node))
			{
				neighbors.add(edge.getDestination());
			}else if(edge.getDestination().equals(node))
			{
				neighbors.add(edge.getSource());
			}
		}
		return neighbors;
	}
	
	private int getWeight(Vertex node, Vertex destination)
	{
		for(Edge edge : _edges)
		{
			if(edge.getSource().equals(node)  && edge.getDestination().equals(destination))
			{
				return edge.getWeight();
			} else if(edge.getDestination().equals(node) && edge.getSource().equals(destination))
			{
				return edge.getWeight();
			}
		}
		return -1; //nodes are not neighbors
	}
	
	public Hashtable<Vertex, Integer> getShortestPaths(Vertex node)
	{
		_distances = new Hashtable<Vertex, Integer>();
		_predecessors = new Hashtable<Vertex, Vertex>();
		_unoptimizedNodes = new ArrayList<Vertex>(_nodes);
		
		for(Vertex vertex : _nodes)
		{
			_distances.put(vertex, Integer.MAX_VALUE);
			
			
		}
		
		//set distance to self to 0
		_distances.put(node, 0);
		
		while(!_unoptimizedNodes.isEmpty())
		{
			Vertex shortest = null;
			int shortestValue = Integer.MAX_VALUE;
			for(Vertex vertex : _unoptimizedNodes)
			{
				if(_distances.get(vertex) < shortestValue)
				{
					shortest = vertex;
					shortestValue = _distances.get(vertex);
				}
			}
			
			//remove shortest from unoptimized nodes list
			_unoptimizedNodes.remove(shortest);
			if(_distances.get(shortest) == Integer.MAX_VALUE)
			{
				break;
			}
			
			int alt;
			for(Vertex vertex : getNeighbors(shortest))
			{
				System.out.println("NEIGHBOR: " + vertex);
				alt = _distances.get(shortest) + getWeight(shortest, vertex);
				
				
				if(alt < _distances.get(vertex))
				{
					_distances.put(vertex, alt);
					_predecessors.put(vertex, shortest);
					_unoptimizedNodes.add(vertex);
				}
			}
		}
		return _distances;
		
	}
	
	public List<Vertex> getPath(Vertex destination)
	{
		List<Vertex> path = new ArrayList<Vertex>();
		
		Vertex curr = destination;
		
		if(_predecessors.get(curr) == null)
		{
			return null;
		}
		
		path.add(curr);
		while(_predecessors.get(curr) != null)
		{
			curr = _predecessors.get(curr);
			path.add(curr);
		}
		
		Collections.reverse(path);
		return path;
	}
	
//	public Stack<Vertex> getShortestPath(Vertex node, Vertex destination)
//	{
//		
//		
//		return null;
//	}
//	
//	public Stack<Vertex> getPath(Vertex source, Vertex destination) {
//		LinkedList<Edge> edges = new LinkedList<Edge>();
//		Stack<Vertex> vertices = new Stack<Vertex>();
//		Vertex curr = source;
//
//		if(!graph.hasVertex(curr) || !graph.hasVertex(destination)) {
//			return null;
//		} else {
//			while(!vertices.contains(destination)) {
//				vertices.push(curr); // add current vertex
//				
//				List<Edge> currEdges = getVertexEdges(curr);
//				
//				Iterator<Edge> edgeIter = edges.iterator(); // iterator for edge list
//				int shortest = edgeIter.next().getWeight(); // temp value for shortest dist
//				Edge shortestEdge = edgeIter.next(); // temp value for shortest edge
//				while(edgeIter.hasNext()) {
//					Edge tmp = edgeIter.next();
//					if(tmp.getWeight() < shortest) { // if the edge is the shortest so far
//						shortest = tmp.getSource(); // set shortest dist
//						shortestEdge = tmp; // set shortest edge
//					}
//				}
//				edges.remove(shortestEdge); // remove the shortest edge from the list
//				if(!shortestEdge.getSource().equals(curr)) {
//					Vertex tmp = curr;
//					while(!tmp.equals(shortestEdge.getSource())) {
//						tmp = vertices.pop(); // pop unneeded vertices
//					}
//				}
//				vertices.push(shortestEdge.getDestination()); // add the path
//			}
//			return vertices;
	
	
//	public static void main(String[] args)
//	{
//		List<Vertex> vertices = new ArrayList<Vertex>();
//		List<Edge> edges = new ArrayList<Edge>();
//		
//		Vertex vertex1 = new Vertex("1", 1);
//		Vertex vertex2 = new Vertex("2", 2);
//		Vertex vertex3 = new Vertex("3", 3);
//		Vertex vertex4 = new Vertex("4", 4);
//		vertices.add(vertex1);
//		vertices.add(vertex2);
//		vertices.add(vertex3);
//		vertices.add(vertex4);
//		
//		Edge edge1 = new Edge(vertex1, vertex2, 1);
//		Edge edge2 = new Edge(vertex1, vertex3, 2);
//		Edge edge3 = new Edge(vertex2, vertex4, 5);
//		Edge edge4 = new Edge(vertex3, vertex4, 3);
//		edges.add(edge1);
//		edges.add(edge2);
//		edges.add(edge3);
//		edges.add(edge4);
//		
//		Graph graph = new Graph(vertices, edges);
//		System.out.println(graph.hasVertex(new Vertex("1", 2)));
//		ShortestPath shortestPath = new ShortestPath(graph);
//		Hashtable<Vertex, Integer> paths = shortestPath.getShortestPaths(vertex4);
//		
//		List<Vertex> temp = new ArrayList<Vertex>(paths.keySet());
//		for(Vertex vertex : temp)
//		{
//			System.out.println(vertex + " " + paths.get(vertex));
//		}
//		
//		System.out.println();
//		
//		List<Vertex> path = shortestPath.getPath(vertex2);
//		for(Vertex vertex : path)
//		{
//			System.out.println(vertex);
//		}
//		
//	}
}
