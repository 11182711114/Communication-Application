package discovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Device;
import parsers.DeviceParser;
import parsers.NmapParser;
import util.Logger;

public class Discovery implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private boolean active = false;
	private ProcessBuilder pb;
	private String command = "nmap", arg1 = "-sn", network = "192.168.1.*", end = "Nmap done:";
	private long tts = 25000;

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

		boolean wait = false;
		// main loop
		while (active) {
			if (wait) {
				try {
					Thread.sleep(tts);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			RoutingTable tmpRT = new RoutingTable(new ArrayList<Device>());
			shellOutput.clear();

			log.debug("Starting Discovery main loop");
			Scanner sc = null;
			try {
				String[] command = pb.command().toArray(new String[0]);
				String com = "";
				for (String s : command) {
					com += s + " ";
				}
				log.debug("Starting nmap process: " + com);
				Process process = pb.start();
				sc = new Scanner(new InputStreamReader(process.getInputStream()));

				boolean endFound = false;
				long startTime = System.currentTimeMillis();
				while (!endFound) {
					// kill the process if it takes longer than 100s
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime - startTime;

					if (timeDiff > (150 * 1000)) {
						log.debug("Taking too long, destroying process");
						process.destroy();
						break;
					}
					// read lines and add them to shellOutput, if the line
					// contains Nmap end signal break the loop
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						log.trace("Reading line: \"" + line + "\"");

						// no need to add the end signal line
						if (line.contains(end)) {
							log.debug("End found, breaking scanning for new lines");
							endFound = true;
							break;
						}
						if (!line.isEmpty())
							shellOutput.add(line);
					}
				}

				// FIXME clean
				log.trace("Parsing shelloutput for valid devices");
				DeviceParser dp = new NmapParser();
				for (String s : shellOutput) {
					try {
						Device dev = dp.parse(s);
						if (dev != null) {
							log.trace("Successfully parsed line into: " + dev.toPrint());
							tmpRT.addDevice(dev);
						}
					} catch (IllegalArgumentException e) {
						log.trace("Cannot parse: " + s + " into valid device");
					}
				}

			} catch (IOException e) {
				log.exception(e);
			}
			if (sc != null)
				sc.close();
			routingTable = tmpRT;
			wait = true;
		}
	}

	public boolean setActive(boolean act) {
		log.debug("Setting active to: " + act);
		active = act;
		return active;
	}
}
