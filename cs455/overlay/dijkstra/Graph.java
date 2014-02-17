package cs455.overlay.dijkstra;

import java.util.ArrayList;
import java.util.List;

public class Graph {
	
	private List<Vertex>	_vertices;
	private List<Edge>		_edges;
	
	public Graph(List<Vertex> vertices, List<Edge> edges)
	{
		_vertices = vertices;
		_edges = edges;
	}
	
	public Graph()
	{
		_vertices = new ArrayList<Vertex>();
		_edges = new ArrayList<Edge>();
	}
	
	public List<Vertex> getVertices()
	{
		return _vertices;
	}
	
	public List<Edge> getEdges()
	{
		return _edges;
	}
	
	public void setVertices(List<Vertex> vertices)
	{
		_vertices = vertices;
	}
	
	public void setEdges(List<Edge> edges)
	{
		_edges = edges;
	}
	
	public void addVertex(Vertex vertex)
	{
		_vertices.add(vertex);
	}
	
	public void addEdge(Edge edge)
	{
		_edges.add(edge);
	}
	
	public void addVertices(List<Vertex> vertices)
	{
		for(Vertex vertex : vertices)
		{
			_vertices.add(vertex);
		}
	}
	
//	public void addVerticesWithStringList(List<String> names)
//	{
//		for(String name : names)
//		{
//			_vertices.add(new Vertex(name));
//		}
//	}
	
	public void addEdges(List<Edge> edges)
	{
		for(Edge edge : edges)
		{
			_edges.add(edge);
		}
	}
	
	public boolean hasVertex(Vertex vertex)
	{
		boolean found = false;
		for(Vertex v : _vertices)
		{
			if(v.equals(vertex))
			{
				found = true;
			}
		}
		return found;
	}
	
	public boolean hasVertex(String ip, int port)
	{
		boolean found = false;
		for(Vertex v : _vertices)
		{
			if(v.getIP().equals(ip) && v.getPort() == port)
			{
				found = true;
			}
		}
		return found;
	}
	
	public Vertex getVertex(String ip, int port)
	{
		for(Vertex vertex : _vertices)
		{
			if(vertex.getIP().equals(ip) && vertex.getPort() == port)
			{
				return vertex;
			}
		}
		return null;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		for(Edge edge : _edges)
		{
			stringBuilder.append(edge);
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}	
}