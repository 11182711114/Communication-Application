package ipc;
import java.io.File;			
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import application.DataPacket;
import application.InputDataPacket;
import application.OutputDataPacket;
import interDeviceCommunication.Channel;
import util.FileUtil;

import java.time.LocalDate;
import java.time.LocalTime;


public class IO {

	private Channel ch;
	static int totalKeyCount= 1;
	ArrayList<Writer>  writerCollection = new ArrayList<>();	
	ArrayList<Reader>  readerCollection = new ArrayList<>();
	
	Map <String, File> input = new HashMap<>() ;
	Map <String, File> output = new HashMap<>();
	

	public void setChannel(Channel ch){
		this.ch = ch;
	}
	
	
	public void handle(InputDataPacket dp){
		LocalDate date = LocalDate.now();
		String dateDescription = "Date: " + date.toString();
		
		LocalTime time = LocalTime.now();
		String timeDescription = "Time: " +time.toString();
		
		
		
		
		
		String pathname = "C:\\Users\\Johan\\IoT\\git\\Communication-Application\\Saves";		// needs to be Linux compatible
		String directory = dp.getDeviceID();
		String fileName = dp.getComID() + ".txt"; //+what? (+"_" + packetID)
		
		pathname = pathname + "\\" + directory;
		
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
		
		input.put("theKey", theFile);				//bullshit code but shows order. if saved in Maps
	}
	

		
		
		
	
	
	public void sendDP(){
		DataPacket toSend = new OutputDataPacket();		// Send to Channel
	}
	
	
	public void createWriter(){
		Writer writer = new Writer();
		writerCollection.add(writer);
	}
	public void createReader(){
		Reader reader = new Reader();
		readerCollection.add(reader);	
	}
}
