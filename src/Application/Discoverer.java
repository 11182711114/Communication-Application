package Application;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Discoverer implements Runnable{
	
	private boolean active = false;
	private ProcessBuilder pb;
	private String command = "nmap";
	private String arg1 = "-sn";
	private String network = "192.168.1.*";
	
	private String end = "Nmap done:";
	
	private List<String> routingTable = new ArrayList<>();
	
	
	public Discoverer(){
		
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
					
					//read lines and add them to shellOutput, if the line contains Nmap end singal break the loop
					while(sc.hasNext()){
						String line = sc.nextLine();
						
						//no need to add the end signgl line
						if(line.contains(end))
							break;
						
						shellOutput.add(line);
					}
				}
				
				//FIXME clean and add to routing table class
				for(String s : shellOutput){
					routingTable.add(s);
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
