package discovery;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Device;
import parsers.DeviceParser;
import parsers.NmapParser;

public class Discovery implements Runnable {
	private util.Logger log = util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();

	private boolean active = false;
	private ProcessBuilder pb;
	private String 
		command = "nmap", 
		arg1 = "-sn", 
		network = "192.168.1.*", 
		end = "Nmap done:";
		

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
		log.info("Starting Discovery", nameForLog);
		active = true;
		// Make the ProcessBuilder
		pb = new ProcessBuilder(command, arg1, network);
		pb.redirectErrorStream(true);

		List<String> shellOutput = new ArrayList<String>();

		// main loop
		while (active) {
			log.debug("Starting Discovery main loop", nameForLog);
			Scanner sc = null;
			try{
				String[] command = pb.command().toArray(new String[0]);
				String com = "";
				for(String s : command){
					com+=s + " ";
				}
				log.debug("Starting nmap process: " + com, nameForLog);
				Process process = pb.start();
				sc = new Scanner(new InputStreamReader(process.getInputStream()));
				
				boolean endFound = false;
				long startTime = System.currentTimeMillis();
				while (!endFound) {
					// kill the process if it takes longer than 100s
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime - startTime;
						
					if (timeDiff > (150 * 1000)) {
						log.debug("Taking too long, destroying process", nameForLog);
						process.destroy();
						break;
					}
					// read lines and add them to shellOutput, if the line
					// contains Nmap end signal break the loop
					while (sc.hasNextLine()) {
						String line = sc.nextLine();
						log.debug("Reading line: \"" + line + "\"", nameForLog);

						// no need to add the end signal line
						if (line.contains(end)){
							log.debug("End found, breaking scanning for new lines", nameForLog);
							endFound = true;
							break;
						}
						if(!line.isEmpty())
							shellOutput.add(line);
					}
				}

				// FIXME clean
				log.info("Parsing shelloutput for valid devices", nameForLog);
				DeviceParser dp = new NmapParser();
				for (String s : shellOutput) {	
					log.debug("Parsing line: \"" + s + "\"", nameForLog);
					try{
						Device dev = dp.parse(s);
						if(dev != null){
							log.debug("Successfully parsed: \"" + s + "\" into: \"" + dev.toPrint(), nameForLog);
							routingTable.addDevice(dev);
						}
					}catch(IllegalArgumentException e){
						log.debug("Cannot parse: " + s + " into valid device", nameForLog);
					}
				}

			} catch (IOException e) {
				log.exception(e);
			}
			if(sc != null)
				sc.close();
			log.debug("Printing devices:", nameForLog);
			for(String s : routingTable.getDevicesAsStrings()){
				log.debug(s, nameForLog);
			}
		}
	}

	public boolean setActive(boolean act) {
		log.debug("Setting active to: " + act, nameForLog);
		active = act;
		return active;
	}
}
