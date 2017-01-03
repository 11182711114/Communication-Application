package dataPacket;

import java.util.ArrayList;
import java.util.List;

import log.Logger;

public abstract class DataPacket {
	private String comID;
	private List<String> data;
	private String deviceID;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	public DataPacket() {
		data = new ArrayList<String>();
	}

	public void setDeviceID(String ID) {
		deviceID = ID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	public void setComID(String comID) {
		this.comID = comID;
	}

	public String getComID() {
		return comID;
	}

	public void saveData(String d) {
		data.add(d);
	}

	public List<String> getData() {
		return data;
	}

	public abstract void parseData(String line);
}