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
	
	
	public Discoverer(){
		
	}

	@Override
	public void run() {
		pb = new ProcessBuilder(command, arg1,network);
		pb.redirectErrorStream(true);
		
		List<String> shellOutput = new ArrayList<String>();
		
		
		while(active){
			
			try {
				Process process = pb.start();

				Scanner sc = new Scanner(new InputStreamReader(process.getInputStream()));
				long startTime = System.currentTimeMillis();
				while(true){
					long currentTime = System.currentTimeMillis();
					long timeDiff = currentTime-startTime;
					if(timeDiff>(100*1000)){
						process.destroy();
						break;
					}
					
					if(sc.hasNext()){
						String line = sc.nextLine();
						
						if(line.contains(end))
							break;
						
						shellOutput.add(line);
					}
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
