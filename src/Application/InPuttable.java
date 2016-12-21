package Application;
import java.io.IOException;

public interface InPuttable {		// för möjligheten av annan I/O hantering
	
	public String read(String filename)throws IOException;
	
	

}