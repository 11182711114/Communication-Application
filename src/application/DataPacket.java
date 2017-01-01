package application;


import java.util.ArrayList;
import java.util.List;

public abstract class DataPacket
{
	private String comID;
	private List<String> data;
	private String deviceID;
	
	private util.Logger log = util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();
	
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
	
	public abstract void parseData(String line);
}