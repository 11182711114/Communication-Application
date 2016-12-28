package application;

import java.util.ArrayList;
import java.util.List;

public abstract class DataPacket
{
	private String comID;
	private List<String> data;
	private String deviceID;
	
	public DataPacket()
	{
		data = new ArrayList<String>();
	}
	
	public void setDeviceID(String ID)
	{
		deviceID = ID;
	}
	
	public String getDeviceID()
	{
		return deviceID;
	}
	
	public void setComID(String comID)
	{
		this.comID = comID;
	}
	
	public String getComID()
	{
		return comID;
	}
	
	public void saveData(String d)
	{
		data.add(d);
	}
	
	public List<String> getData()
	{
		return data;
	}
	
	public void checkSum(String checkSum)
	{
		System.out.println("DO STUFF HERE? (cheksum)");
	}
	
	public abstract void parseData(String line);
}