package parsers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import interDeviceCommunication.Device;

public class NmapParser implements DeviceParser {

	String relLine = "Nmap scan report for ";

	/**
	 * Parses a string from Nmap into Device
	 * 
	 * @param s
	 *            - The string to be parsed
	 * @return Device with the given IP
	 * @throws IllegalArgumentException
	 *             - if the given string cannot be parsed
	 */
	@Override
	public Device parse(String s) throws UnknownHostException, IllegalArgumentException {
		if (!s.contains(relLine))
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");

		Pattern ipRegex = Pattern.compile("(([0-9]{1,3}\\.){3}[0-9]{1,3})");
		Matcher m = ipRegex.matcher(s);
		if (!m.find())
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");

		InetAddress ip = InetAddress.getByName(m.group());

		Device d = new Device(ip);
		return d;
	}

	/**
	 * Parses a string from Nmap into Device with the given Device ID
	 *
	 * @param deviceID
	 *            - The Device ID to be given the device
	 * @param s
	 *            - The string to be parsed
	 * @return Device with the given IP
	 * @throws IllegalArgumentException
	 *             - if the given string cannot be parsed
	 */


}
