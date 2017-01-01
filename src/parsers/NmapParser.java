package parsers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Device;

public class NmapParser implements DeviceParser {

	String relLine = "Nmap scan report for ";

	/**
	 * Parses a string from Nmap into Device
	 * 
	 * @param s - The string to be parsed
	 * @return Device with the given IP
	 * @throws IllegalArgumentException - if the given string cannot be parsed
	 */
	@Override
	public Device parse(String s) throws UnknownHostException, IllegalArgumentException {
		if (!s.contains(relLine))
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");
		
		Pattern ipRegex = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
		Matcher m = ipRegex.matcher(s);
		if(!m.find())
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");
			
		InetAddress ip = InetAddress.getByName(m.group(1));
		
//		int openPar = s.indexOf("(") + 1;
//		int closePar = s.indexOf(")") - 1;
//		if(openPar<0 || closePar<0)
//			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");
//		
//		InetAddress ip = InetAddress.getByName(s.substring(openPar,closePar));
		Device d = new Device(ip);
		return d;
	}
	
	/**
	 * Parses a string from Nmap into Device with the given Device ID
	 *
	 * @param deviceID - The Device ID to be given the device
	 * @param s - The string to be parsed
	 * @return Device with the given IP
	 * @throws IllegalArgumentException - if the given string cannot be parsed
	 */
	@Override
	public Device parse(String deviceID, String s) throws UnknownHostException, IllegalArgumentException {
		if (!s.contains(relLine))
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");

		int openPar = s.indexOf("(") + 1;
		int closePar = s.indexOf(")") - 1;
		if(openPar<0 || closePar<0)
			throw new IllegalArgumentException("The given string\"" + s + "\" cannot be parsed into a valid device");
		InetAddress ip = InetAddress.getByName(s.substring(openPar,closePar));
		Device d = new Device(ip, deviceID);
		return d;
	}

}
