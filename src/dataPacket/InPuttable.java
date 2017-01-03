package dataPacket;

import java.io.IOException;
import java.io.File;

public interface InPuttable { // f�r m�jligheten av annan I/O hantering

	public String[] read(File filename) throws IOException;

}