package Application;

import java.io.IOException;

public interface OutPuttable {		// för möjligheten av annan I/O hantering
	
	public void write(String [] input)throws IOException;
	

}