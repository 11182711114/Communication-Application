package ipc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import util.FileUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;
import log.Logger;

public class IO {
	private final String directoryPath;
	private final String comID;
	private Logger log;

	public IO(File directoryPath) {
		this.directoryPath = directoryPath.getAbsolutePath() + File.separator;
		comID = directoryPath.getAbsolutePath()
				.substring(directoryPath.getAbsolutePath().lastIndexOf(File.separator) + 1);
		log = Logger.getLogger(this.getClass().getSimpleName() + "@" + comID);
	}

	private int getFileCount(String path) {

		File dir = new File(path);
		if (!dir.exists()) {
			return 1;
		}
		return dir.listFiles().length + 1;
	}

	public void handle(InputDataPacket dp) {
		LocalDate date = LocalDate.now();
		String dateDescription = "Date: " + date.toString();
		LocalTime time = LocalTime.now();
		String timeDescription = "Time: " + time.toString();

		String fullPathname = directoryPath + "Input/";

		String fileName = getFileCount(fullPathname) + ".txt";

		File theFile = new File(fullPathname, fileName);

		try {
			FileUtil.writeToFile(dateDescription, theFile);
			FileUtil.writeToFile(timeDescription, theFile);
			FileUtil.writeToFile("Data{\n", theFile);

			for (String s : dp.getData()) {
				FileUtil.writeToFile(s, theFile);

			}
			FileUtil.writeToFile("\n}", theFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private OutputDataPacket createOutputDataPacket(String[] data) {
		OutputDataPacket toSend = new OutputDataPacket(); // Send to Channel
		toSend.setComID(comID);
		for (int i = 1; i < data.length; i++) {
			toSend.parseData(data[i]);
		}

		return toSend;
	}

	public boolean checkForOutput() {
		File dir = new File(directoryPath + "Output/Send/");

		if (dir.exists()) {
			if (dir.listFiles().length > 0) {
				log.debug("Output: return true");
				return true;
			}
		}
		return false;
	}

	public OutputDataPacket[] sendDataPackets() throws FileNotFoundException {
		ArrayList<OutputDataPacket> packets = new ArrayList<>();

		File dir = new File(directoryPath + "Output/Send/");

		for (File f : dir.listFiles()) {
			String[] fileContent = FileUtil.readFromFile(f);

			if (fileContent.length > 1) {
				OutputDataPacket thePacket = createOutputDataPacket(fileContent);
				packets.add(thePacket);
				moveToSent(f);
			}
		}
		return packets.toArray(new OutputDataPacket[0]);
	}

	public void moveToSent(File f) {

		f.renameTo(new File(directoryPath + "Output/Sent/" + f.getName()));

	}

	public String getComID() {
		return comID;
	}
}