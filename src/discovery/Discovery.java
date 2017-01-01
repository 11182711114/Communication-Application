package discovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Device;
import parsers.DeviceParser;
import parsers.NmapParser;
import util.Logger;

/**
 * @author Fredrik
 *
 */
public class Discovery implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());
	
	private static final long PROCESS_MAX_TIME_IN_MS = 150000;
	private static final long TIME_BETWEEN_SCANS_IN_MS = 10000;

	private boolean active = false;
	private ProcessBuilder pb;
	private String command = "nmap", arg1 = "-sn", network = "192.168.1.*", end = "Nmap done:";
	private RoutingTable routingTable;

	public Discovery(RoutingTable rt, String network) {
		this.routingTable = rt;
		this.network = network;
	}

	public Discovery(RoutingTable rt) {
		this.routingTable = rt;
	}

	@Override
	public void run() {
		log.info("Starting Discovery");
		active = true;
		// Make the ProcessBuilder
		pb = new ProcessBuilder(command, arg1, network);
		pb.redirectErrorStream(true);

		List<String> shellOutput = new ArrayList<String>();

		// main loop
		while (active) {
			shellOutput.clear();

			log.debug("Starting Discovery main loop");
			Scanner sc = null;
			try{
				String[] command = pb.command().toArray(new String[0]);
				String com = "";
				for (String s : command) {
					com += s + " ";
				}
				log.debug("Starting nmap process: " + com);
				
				Process process = pb.start();
				sc = new Scanner(new InputStreamReader(process.getInputStream()));

				long startTime = System.currentTimeMillis();
				boolean endFound = false;
				
				while (!endFound) {
					// kill the process if it takes longer than 150s
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime - startTime;

					if (timeDiff > PROCESS_MAX_TIME_IN_MS) {
						log.debug("Taking too long, destroying process");
						process.destroy();
						break;
					}
					endFound = scanData(sc,shellOutput);
				}
				
				Device[] devices = parseDevices(shellOutput);
				routingTable.updateDevices(devices);
				
				Thread.sleep(TIME_BETWEEN_SCANS_IN_MS);
			} catch (IOException e) {
				log.exception(e);
			} catch (InterruptedException e) {
				log.exception(e);
			} finally {
				if (sc != null)
					sc.close();
			}
		}
	}
	/** Parses a List<String> into devices
	 * @param toParse - a List<String> to attempt to parse into Devices
	 * @return an array of Device containing all parsable devices from toParse
	 */
	private Device[] parseDevices(List<String> toParse){
		// FIXME clean
		List<Device> devices = new ArrayList<>();
		log.trace("Parsing shelloutput for valid devices");
		DeviceParser dp = new NmapParser();
		for (String s : toParse) {
			try {
				Device dev = dp.parse(s);
				if (dev != null) {
					log.trace("Successfully parsed line into: " + dev.toPrint());
					devices.add(dev);
				}
			} catch (IllegalArgumentException e) {
				log.trace("Cannot parse: " + s + " into valid device");
			} catch (UnknownHostException e) {
				log.exception(e);
			}
		}
		return devices.toArray(new Device[0]);
	}
	/**Scans lines from a Scanner and adds them to a List<String>
	 * @param sc - the Scanner from which to read
	 * @param shellOutput - the List<String> to add the lines
	 * @return true if end is found, false otherwise
	 */
	private boolean scanData(Scanner sc, List<String> shellOutput) {
		// read lines and add them to shellOutput, if the line
		// contains Nmap end signal break the loop
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			log.trace("Reading line: \"" + line + "\"");

			// no need to add the end signal line
			if (line.contains(end)) {
				log.debug("End found, breaking scanning for new lines");
				return true;
			}
			if (!line.isEmpty())
				shellOutput.add(line);
		}
		return false;
	}

	public boolean setActive(boolean act) {
		log.debug("Setting active to: " + act);
		active = act;
		return active;
	}
}
