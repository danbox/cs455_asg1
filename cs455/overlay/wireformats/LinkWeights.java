package cs455.overlay.wireformats;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LinkWeights implements Event
{
	private final int 		_TYPE = Protocol.LINK_WEIGHTS;
	private int				_linkCount;
	private List<LinkInfo>	_links;
	
	public LinkWeights()
	{
		_links = new ArrayList<LinkInfo>();
	}
	
	public LinkWeights(int linkCount, List<LinkInfo> links)
	{
		_linkCount = linkCount;
		_links = links;
	}
	
	public LinkWeights(byte[] marshalledBytes) throws IOException
	{
		ByteArrayInputStream baInputStream = new ByteArrayInputStream(marshalledBytes);
		DataInputStream din = new DataInputStream(new BufferedInputStream(baInputStream));
		
		int type = din.readInt(); //read int for type
        if(type != _TYPE) //invalid type
        {
            System.out.println("Invalid type");
            return;
        }
		
        _linkCount = din.readInt();
        
        _links = new ArrayList<LinkInfo>();
        for(int i = 0; i < _linkCount; ++i)
        {
        	int sourceIPLength = din.readInt();
        	byte[] sourceIPBytes = new byte[sourceIPLength];
        	din.readFully(sourceIPBytes);
        	String sourceIP = new String(sourceIPBytes);
        	
        	int sourcePort = din.readInt();
        	
        	int sourceListeningPort = din.readInt();

        	int destinationIPLength = din.readInt();
        	byte[] destinationIPBytes = new byte[destinationIPLength];
        	din.readFully(destinationIPBytes);
        	String destinationIP = new String(destinationIPBytes);
        	
        	int destinationPort = din.readInt();
        	
        	int destinationListeningPort = din.readInt();
        	
        	int linkWeight = din.readInt();
        	
        	_links.add(new LinkInfo(sourceIP, sourcePort, sourceListeningPort, destinationIP, destinationPort, destinationListeningPort, linkWeight));
        }
		
		baInputStream.close();
		din.close();
	}
	
	@Override
	public int getType() {
		return _TYPE;
	}
	
	public void setLinkCount(int linkCount)
	{
		_linkCount = linkCount;
	}
	
	public int getLinkCount()
	{
		return _linkCount;
	}
	
	public void setLinks(List<LinkInfo> links)
	{
		_links = links;
	}

	public List<LinkInfo> getLinks()
	{
		return _links;
	}
	@Override
	public byte[] getBytes() throws IOException
	{
		byte[] marshalledBytes = null;
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(new BufferedOutputStream(baOutputStream));
		
		dout.writeInt(_TYPE);
		
		dout.writeInt(_links.size());
		
		for(LinkInfo linkInfo : _links)
		{
			byte[] sourceIPBytes = linkInfo.get_sourceIP().getBytes();
			int elementLength = sourceIPBytes.length;
			dout.writeInt(elementLength);
			dout.write(sourceIPBytes);
			
			dout.writeInt(linkInfo.get_sourcePort());
			
			dout.writeInt(linkInfo.get_sourceListeningPort());
			
			byte[] destinationIPBytes = linkInfo.get_destinationIP().getBytes();
			elementLength = destinationIPBytes.length;
			dout.writeInt(elementLength);
			dout.write(destinationIPBytes);
			
			dout.writeInt(linkInfo.get_destinationPort());
			
			dout.writeInt(linkInfo.get_destinationListeningPort());
			
			dout.writeInt(linkInfo.get_weight());
		}
		
		dout.flush();
		marshalledBytes = baOutputStream.toByteArray();
		
		baOutputStream.close();
		dout.close();
		
		return marshalledBytes;
	}

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Number of links: ");
        stringBuilder.append(_linkCount);
        stringBuilder.append("\n");
        for(LinkInfo linkInfo : _links)
        {
        	stringBuilder.append(linkInfo);
        	stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
