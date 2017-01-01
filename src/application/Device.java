package application;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class Device implements Comparable<Device> {
	private String deviceID;
	private InetAddress ip;

	public Device(InetAddress ip, String deviceID) {
		this.deviceID = deviceID;
		this.ip = ip;
	}

	public Device(InetAddress ip) {
		this.ip = ip;
	}

	public Device(String deviceID, String IP) {
		this.deviceID = deviceID;
		try {
			this.ip = InetAddress.getByName(IP);
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
		return ip;
	}

	public void setIP(InetAddress iP) {
		ip = iP;
	}

	@Override
	public int compareTo(Device d) {
		return this.deviceID.compareTo(d.getDeviceID());
	}

	public String toPrint() {
		return "ID: " + deviceID + " IP:" + ip.toString();
	}

}
