package Application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import parsers.DeviceParser;
import parsers.NmapParser;

public class Discoverer implements Runnable{
	
	private boolean active = false;
	private ProcessBuilder pb;
	private String command = "nmap";
	private String arg1 = "-sn";
	private String network = "192.168.1.*";
	
	private String end = "Nmap done:";
	
	private RoutingTable routingTable;
	
	
	public Discoverer(RoutingTable rt){
		this.routingTable = rt;
		
	}

	@Override
	public void run() {
		//Make the ProcessBuilder
		pb = new ProcessBuilder(command, arg1,network);
		pb.redirectErrorStream(true);
		
		List<String> shellOutput = new ArrayList<String>();
		
		//main loop
		while(active){
			
			try {
				Process process = pb.start();

				Scanner sc = new Scanner(new InputStreamReader(process.getInputStream()));
				long startTime = System.currentTimeMillis();
				while(true){
					//kill the process if it takes longer than 100s
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime-startTime;
					if(timeDiff>(100*1000)){
						process.destroy();
						break;
					}
					
					//read lines and add them to shellOutput, if the line contains Nmap end signal break the loop
					while(sc.hasNext()){
						String line = sc.nextLine();
						
						//no need to add the end signal line
						if(line.contains(end))
							break;
						
						shellOutput.add(line);
					}
				}
				
				//FIXME clean
				DeviceParser dp = new NmapParser();
				try{
					for(String s : shellOutput){
						Device dev = dp.parse(s);
						routingTable.addDevice(dev);
					}
				}catch(IllegalArgumentException e){
					e.printStackTrace();
				}
			
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	public boolean setActive(boolean act){
		active = act;
		return active;
	}
}
