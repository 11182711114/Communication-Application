package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil {
	public static void writeToFile(String toWrite, File toWriteIn) throws IOException {
		if (!toWriteIn.exists()) {
			new File(toWriteIn.getAbsolutePath().substring(0, toWriteIn.getAbsolutePath().lastIndexOf("\\"))).mkdirs();
		}

		PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(toWriteIn, true)));
		pw.println(toWrite);
		pw.flush();
		pw.close();
	}
}