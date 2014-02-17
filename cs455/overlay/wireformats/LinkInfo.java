package cs455.overlay.wireformats;

public class LinkInfo {

	private String 		_sourceIP;
	private int			_sourcePort;
	private String		_destinationIP;
	private int			_destinationPort;
	private int			_weight;
	
	public LinkInfo()
	{
		_sourceIP = new String();
		_destinationIP = new String();
	}
	
	public LinkInfo(String sourceIP, int sourcePort, String destinationIP, int destinationPort, int weight)
	{
		_sourceIP = sourceIP;
		_sourcePort = sourcePort;
		_destinationIP = destinationIP;
		_destinationPort = destinationPort;
		_weight = weight;
	}

	public String get_sourceIP()
	{
		return _sourceIP;
	}

	public void set_sourceIP(String _sourceIP) 
	{
		this._sourceIP = _sourceIP;
	}

	public int get_sourcePort() 
	{
		return _sourcePort;
	}

	public void set_sourcePort(int _sourcePort) 
	{
		this._sourcePort = _sourcePort;
	}

	public String get_destinationIP() 
	{
		return _destinationIP;
	}

	public void set_destinationIP(String _destinationIP) 
	{
		this._destinationIP = _destinationIP;
	}

	public int get_destinationPort() 
	{
		return _destinationPort;
	}

	public void set_destinationPort(int _destinationPort) 
	{
		this._destinationPort = _destinationPort;
	}

	public int get_weight() 
	{
		return _weight;
	}

	public void set_weight(int _weight) 
	{
		this._weight = _weight;
	}
	
	@Override
	public String toString()
	{
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(_sourceIP);
		stringBuilder.append(":");
		stringBuilder.append(_sourcePort);
		stringBuilder.append(" ");
		stringBuilder.append(_destinationIP);
		stringBuilder.append(":");
		stringBuilder.append(_destinationPort);
		stringBuilder.append(" ");
		stringBuilder.append(_weight);
		return stringBuilder.toString();
	}
	
}
