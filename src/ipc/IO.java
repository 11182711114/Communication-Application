package ipc;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import application.DataPacket;
import application.InputDataPacket;
import application.OutputDataPacket;
import interDeviceCommunication.Channel;
import util.FileUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;


public class IO {

//	private Channel ch;
	
/*
	static int totalKeyCount= 1;
	ArrayList<Writer>  writerCollection = new ArrayList<>();	
	ArrayList<Reader>  readerCollection = new ArrayList<>();
	
	Map <String, File> input = new HashMap<>() ;
	Map <String, File> output = new HashMap<>();
	
*/	
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
		
		
		String pathname = "./files";		// needs to be Linux compatible
		String directory = dp.getDeviceID();
		String comDir = dp.getComID();
		
		pathname = pathname + "\\" + directory + "\\" + comDir;
		
		
		String fileName = getFileCount(pathname) + ".txt"; 
		
		File theFile = new File(pathname, fileName);
		
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
		
		//input.put("theKey", theFile);				//bullshit code but shows order. if saved in Maps
	}
	
	private OutputDataPacket createOutputDataPacket(String[] data){
		OutputDataPacket toSend = new OutputDataPacket();		// Send to Channel
		for(int i = 1 ; i< data.length ; i++){
			toSend.parseData(data[i]);	
		}
		
		return toSend;
	}
	
	public boolean checkForOutput(String deviceID)
	{
		File dir = new File("C:\\Users\\Johan\\IoT\\git\\Communication-Application\\files\\" + deviceID + "\\pending");
		
		if(dir.exists())
		{
			if(dir.listFiles().length > 0)
			{
				return true;
			}
		}
		
		return false;
	}
	
	public OutputDataPacket[] sendDataPackets(String deviceID) throws FileNotFoundException
	{
		ArrayList<OutputDataPacket> packets = new ArrayList<>();
		
		File dir = new File("C:\\Users\\Johan\\IoT\\git\\Communication-Application\\files\\" + deviceID + "\\pending");
		
		for(File f : dir.listFiles()){
			String[] fileContent = FileUtil.readFromFile(f);
			
			if(fileContent.length >1){
				
				OutputDataPacket thePacket = createOutputDataPacket(fileContent);
				packets.add(thePacket);
				moveToSend(f, deviceID);
			}
		}
		
		return packets.toArray(new OutputDataPacket[0]);
		// ./ root for program / file/ dev1/
	}
	
	
	public void moveToSend(File f, String deviceID){
		
		
		f.renameTo(new File("C:\\Users\\Johan\\IoT\\git\\Communication-Application\\files\\" + deviceID + "\\sent\\" +f.getName()));
	}
	
/*	Not currently relevant.
 
	public void createWriter(){
		Writer writer = new Writer();
		writerCollection.add(writer);
	}
	public void createReader(){
		Reader reader = new Reader();
		readerCollection.add(reader);	
	}
*/
}
