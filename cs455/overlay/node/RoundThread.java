package cs455.overlay.node;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import cs455.overlay.dijkstra.Vertex;
import cs455.overlay.transport.Connection;
import cs455.overlay.wireformats.Message;

public class RoundThread extends Thread 
{

	private MessagingNode _node;

	public RoundThread(MessagingNode node)
	{
		_node = node;
	}

	@Override
	public void run()
	{
		//get random target
		for(int round = 0; round < 500; ++round)
		{
			System.out.println(round);
			List<Vertex> nodes = _node.get_routingCache().getNodes();
			int targetIndex = (int)(1 + Math.random() * (nodes.size() - 1));
			Vertex target = nodes.get(targetIndex);

			//set path
			LinkedList<Vertex> path = _node.get_routingCache().getPath(target);

			while(path.size() == 1) //chose self
			{
				targetIndex = (int)(1 + Math.random() * (nodes.size() - 1));
				target = nodes.get(targetIndex);
				path = _node.get_routingCache().getPath(target);
			}

			//remove self from path
			path.poll();
			//get next node in path
			Vertex next = path.element();


			//get connection
			Connection conn = _node.get_connections().get(next.getIP() + ":" + next.getListeningPort());
			if(conn == null)
			{
				conn = _node.get_connections().get(next.getIP() + ":" + next.getPort());
			}
			//			System.out.println(conn);

			//send 5 messages per rounds
			Random random = new Random();
			for(int i = 0; i < 5; ++i)
			{
				int payload = random.nextInt();
				_node.set_sendTracker(_node.get_sendTracker() + 1);
				_node.set_sendSummation(_node.get_sendSummation() + payload);
				Message message = new Message(payload, path);
				try
				{
					conn.sendData(message.getBytes());
				}catch(IOException ioe)
				{
					ioe.printStackTrace();
				}
			}
			try
			{
				Thread.sleep(50);
			}catch(InterruptedException uhe)
			{
				uhe.printStackTrace();
			}
		}
		_node.sendTaskComplete();
	}
}
