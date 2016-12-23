package Application;

import java.io.IOException;

public interface OutPuttable { // f�r m�jligheten av annan I/O hantering

	public void write(String[] input) throws IOException;

}