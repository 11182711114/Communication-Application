package parsers;

import java.net.UnknownHostException;

import interDeviceCommunication.Device;

public interface DeviceParser {
	Device parse(String s) throws UnknownHostException, IllegalArgumentException;

	Device parse(String deviceId, String s) throws UnknownHostException, IllegalArgumentException;

}
