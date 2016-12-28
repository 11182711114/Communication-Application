package application;

import java.net.InetAddress;
import java.util.ArrayList;

public class OutputDataPacket extends DataPacket
{
	private InetAddress adress;
	
	public void setInetAdress(InetAddress adress)
	{
		this.adress = adress;
	}
	
	public InetAddress getAdress()
	{
		return adress;
	}
	
	@Override
	public void parseData(String line) 
	{
		String data = "<DATA> " + line;
		
		saveData(data);
	}
	
	public String[] toSend()
	{
		ArrayList<String> toSend = new ArrayList<>();
		
		toSend.add(getComID());
		toSend.add(getDeviceID());
		
		for(String d : getData())
		{
			toSend.add("<DATA> " + d);
		}
		
		return toSend.toArray(new String[toSend.size()]);
	}
}