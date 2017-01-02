package interDeviceCommunication;

public class DeviceIdDeterminer {
	
	public static Device determineDeviceId(Device d){
		Device output = new Device(d.getIP());
		
		
		return output;		
	}
}
