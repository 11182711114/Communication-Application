package Application;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader implements InPuttable {

	private int readerId = 1;
	public static int readerCount;

	public Reader() {
		readerCount = readerId;
		readerId = readerId++;
	}

	@Override
	public String read(String inputFileName) throws IOException {

		String content = null;
		File file = new File(inputFileName);
		FileReader fileReader = null;

		// try{
		//
		// }catch (IOException e){ ?? ist�llet f�r throw?
		//
		// }

		fileReader = new FileReader(file);
		char[] chars = new char[(int) file.length()];
		fileReader.read(chars);
		content = new String(chars);
		fileReader.close();

		return content;
	}
}
