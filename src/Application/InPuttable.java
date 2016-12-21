package Application;
import java.io.IOException;
import java.io.File;

public interface InPuttable {		// för möjligheten av annan I/O hantering
	
	public String[] read(File filename)throws IOException;
	
	

}