package parsers;

import java.net.InetAddress;
import java.net.UnknownHostException;

import Application.Device;

public class NmapParser implements DeviceParser {

	@Override
	public Device parse(String s) throws UnknownHostException, IllegalArgumentException {
		String[] tmp = s.split(" ");
		if (tmp[1] != "scan")
			throw new IllegalArgumentException("Not a scan string");
		InetAddress ip = InetAddress.getByName(tmp[5]);
		Device d = new Device(ip);
		return d;
	}

	@Override
	public Device parse(String deviceID, String s) throws UnknownHostException, IllegalArgumentException {
		String[] tmp = s.split(" ");
		if (tmp[1] != "scan")
			throw new IllegalArgumentException("Not a scan string");
		InetAddress ip = InetAddress.getByName(tmp[5]);
		Device d = new Device(deviceID, ip);
		return d;
	}

}
