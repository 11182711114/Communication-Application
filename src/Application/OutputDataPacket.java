package Application;

import java.net.InetAddress;

public class OutputDataPacket extends DataPacket
{
	private InetAddress adress;
	
	public void setInetAdress(InetAddress adress)
	{
		this.adress = adress;
	}
	
	@Override
	public void parseData(String line) 
	{
		String data = "<DATA> " + line;
		
		saveData(data);
	}
	
	public String[] dataToSend()
	{
		//FIXA!!
		return null;
	}
}