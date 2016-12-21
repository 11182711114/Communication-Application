package parsers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Application.Device;

public class NmapParser implements DeviceParser{
	
	
	
	@Override
	public Device parse(String s) throws UnknownHostException {
		String[] tmp = s.split(" ");
		InetAddress ip = InetAddress.getByName(tmp[5]);
		Device d = new Device(ip);
		return d;
	}

	@Override
	public Device parse(String deviceID, String s) throws UnknownHostException {
		String[] tmp = s.split(" ");
		InetAddress ip = InetAddress.getByName(tmp[5]);
		Device d = new Device(deviceID,ip);
		return d;
	}

}
