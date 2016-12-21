package Application;

//import java.nio.file.Files;
//import java.io.FileInputStream;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader implements InPuttable{
	
	public Reader(){
		
	}

	@Override
	public String[] read(File file) throws IOException{
		
		ArrayList <String> lineList = new ArrayList<>();
		//läs rad för rad
		//spara i en List
		//returnera en String[]
		
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
		    for(String line; (line = br.readLine()) != null; ) {
		    	
		    	lineList.add(line);
		        // process the line.
		    }
		     
		    // line is not visible here.
		}// catch?? istället för throw?
		
		String[] lines = new String [lineList.size()];
		lineList.toArray(lines);	
		
		
		
		return lines;
		
//		1st version
//		fileReader = new FileReader(file);
//		char[] chars = new char [(int) file.length()];
//		fileReader.read(chars);
//		content = new String (chars);
//		fileReader.close();
	}
}
