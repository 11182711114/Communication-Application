package Application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import parsers.DeviceParser;
import parsers.NmapParser;

public class Discovery implements Runnable {
	private Util.Logger log = Util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();

	private boolean active = false;
	private ProcessBuilder pb;
	private String 
		command = "nmap", 
		arg1 = "-sn", 
		network = "192.168.1.*", 
		end = "Nmap done:";
		

	private RoutingTable routingTable;

	public Discovery(RoutingTable rt) {
		this.routingTable = rt;

	}

	@Override
	public void run() {
		log.info("Starting Discovery", nameForLog);
		// Make the ProcessBuilder
		pb = new ProcessBuilder(command, arg1, network);
		pb.redirectErrorStream(true);

		List<String> shellOutput = new ArrayList<String>();

		// main loop
		while (active) {
			Scanner sc = null;
			try{
				Process process = pb.start();
				sc = new Scanner(new InputStreamReader(process.getInputStream()));
				
				long startTime = System.currentTimeMillis();
				while (true) {
					// kill the process if it takes longer than 100s
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime - startTime;
					if (timeDiff > (100 * 1000)) {
						process.destroy();
						break;
					}

					// read lines and add them to shellOutput, if the line
					// contains Nmap end signal break the loop
					while (sc.hasNext()) {
						String line = sc.nextLine();
						log.debug("Reading line: \"" + line + "\"", nameForLog);

						// no need to add the end signal line
						if (line.contains(end)){
							log.debug("End found, breaking scanning for new lines", nameForLog);
							break;
						}

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
					else{
						log.debug("Output is null, not adding to RoutingTable", nameForLog);
					}
					}catch(IllegalArgumentException e){
						log.error("Parsing failed for \""+ s + "\"", nameForLog);
						
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
			if(sc != null)
				sc.close();

		}
	}

	public boolean setActive(boolean act) {
		active = act;
		return active;
	}
}
