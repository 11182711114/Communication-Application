package ipc;

import java.io.FileWriter;
import java.io.IOException;

import application.OutPuttable;

public class Writer implements OutPuttable {

	public Writer() {

	}

	@Override
	public void write(String[] input) throws IOException {

		FileWriter filewriter = new FileWriter("Writer" + "_Output.txt ");

		for (String out : input) {
			filewriter.write(out);
		}

		filewriter.close();
	}
}
