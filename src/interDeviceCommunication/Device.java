package interDeviceCommunication;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Device implements Comparable<Device> {
	private InetAddress ip;


	public Device(InetAddress ip) {
		this.ip = ip;
	}

	public Device(String IP) {
		try {
			this.ip = InetAddress.getByName(IP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public String getDeviceID() {
		return ip.getHostAddress();
	}

	public InetAddress getIP() {
		return ip;
	}

	public void setIP(InetAddress iP) {
		ip = iP;
	}

	@Override
	public int compareTo(Device d) {
		return this.getDeviceID().compareTo(d.getDeviceID());
	}

	public String toPrint() {
		return "IP=" + ip.toString().substring(1);
	}

}
