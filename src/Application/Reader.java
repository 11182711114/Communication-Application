package Application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader implements InPuttable{
	
	public Reader(){
		
	}

	@Override
	public String read(File file) throws IOException{
		
		String content = null;
		FileReader fileReader = null;
		
		
		
//		try{
//			
//		}catch (IOException e){			?? istället för throw?
//			
//		}
		
		fileReader = new FileReader(file);
		char[] chars = new char [(int) file.length()];
		fileReader.read(chars);
		content = new String (chars);
		fileReader.close();
		
		return content;
	}
}
