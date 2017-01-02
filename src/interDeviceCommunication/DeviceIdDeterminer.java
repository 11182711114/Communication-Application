package interDeviceCommunication;

import java.net.Socket;

public class DeviceIdDeterminer {
	
	public static Device determineDeviceId(Device d){
		Device output = new Device(d.getIP());
		
		
		return output;		
	}
	
	public static Device giveDeviceId(Socket s){
		Device output = new Device(s.getInetAddress());
		
		return output;
	}
}
