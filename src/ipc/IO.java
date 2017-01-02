package ipc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import util.FileUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;


public class IO {
	private final String directoryPath;		// needs to be Linux compatible  = "./files"?
	
	public IO (String directoryPath){
		this.directoryPath = directoryPath;
	}
	
	private int getFileCount(String path){
		
		File dir = new File (path);
		if(!dir.exists()){
			return 1;
		}
		return dir.listFiles().length+1;
	}
	
	
	public void handle(InputDataPacket dp){
		LocalDate date = LocalDate.now();
		String dateDescription = "Date: " + date.toString();
		LocalTime time = LocalTime.now();
		String timeDescription = "Time: " +time.toString();
		
		
		
		String deviceDir = dp.getDeviceID();
		String comDir = dp.getComID();
		
		String fullPathname = directoryPath + "\\Input\\" + deviceDir + "\\" + comDir;
		
		
		String fileName = getFileCount(fullPathname) + ".txt"; 
		
		File theFile = new File(fullPathname, fileName);
		
		try{
			FileUtil.writeToFile(dateDescription, theFile);
			FileUtil.writeToFile(timeDescription, theFile);
			FileUtil.writeToFile("Data{\n", theFile);
			
			for(String s: dp.getData()){
				FileUtil.writeToFile(s,theFile);
				
			}
			FileUtil.writeToFile("\n}", theFile);
			
			}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private OutputDataPacket createOutputDataPacket(String[] data){
		OutputDataPacket toSend = new OutputDataPacket();		// Send to Channel
		for(int i = 1 ; i< data.length ; i++){
			toSend.parseData(data[i]);	
		}
		
		return toSend;
	}
	
	public boolean checkForOutput(String deviceID, String comID)
	{
		File dir = new File(directoryPath+ "Output\\Send\\" + deviceID + "\\"+comID);
		
		if(dir.exists())
		{
			if(dir.listFiles().length > 0)
			{
				return true;
			}
		}
		return false;
	}
	
	public OutputDataPacket[] sendDataPackets(String deviceID, String comID) throws FileNotFoundException
	{
		ArrayList<OutputDataPacket> packets = new ArrayList<>();
		
		File dir = new File(directoryPath+ "Output\\Send\\" + deviceID + "\\"+comID);
		
		for(File f : dir.listFiles()){
			String[] fileContent = FileUtil.readFromFile(f);
			
			if(fileContent.length >1){
				
				OutputDataPacket thePacket = createOutputDataPacket(fileContent);
				packets.add(thePacket);
				moveToSend(f, deviceID, comID);
			}
		}
		
		return packets.toArray(new OutputDataPacket[0]);
	}
	
	
	public void moveToSend(File f, String deviceID, String comID){
		
		f.renameTo(new File(directoryPath+ "Output\\Sent\\" + deviceID + "\\" + comID + "\\" + f.getName()));
		
	}
}