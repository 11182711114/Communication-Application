package Application;

import java.io.IOException;

public interface InPuttable { // f�r m�jligheten av annan I/O hantering

	public String read(String filename) throws IOException;

}