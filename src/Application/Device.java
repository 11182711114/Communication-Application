package Application;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Device implements Comparable<Device> {
	private String deviceID;
	private InetAddress IP;
	
	public Device(String deviceID, InetAddress IP){
		this.deviceID = deviceID;
		this.IP = IP;
	}
	public Device(String deviceID, String IP){
		this.deviceID = deviceID;
		try {
			this.IP = InetAddress.getByName(IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public InetAddress getIP() {
		return IP;
	}
	public void setIP(InetAddress iP) {
		IP = iP;
	}
	@Override
	public int compareTo(Device d) {
		return this.deviceID.compareTo(d.getDeviceID());
	}

}
