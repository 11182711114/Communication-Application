package Application;

import java.io.FileWriter;
import java.io.IOException;

public class Writer implements OutPuttable {

	private int writerId = 1;
	public static int writerCount;

	public Writer() {
		writerCount = writerId;
		writerId = writerId++;

	}

	@Override
	public void write(String[] input) throws IOException {

		FileWriter filewriter = new FileWriter("Writer: " + writerId + "_Output.txt ");

		for (String out : input) {
			filewriter.write(out);
		}

		filewriter.close();
	}
}
