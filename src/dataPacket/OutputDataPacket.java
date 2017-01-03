package dataPacket;

import java.util.ArrayList;

public class OutputDataPacket extends DataPacket {

	@Override
	public void parseData(String line) {
		String data = "<DATA> " + line + "\n";

		saveData(data);
	}

	public String[] toSend() {
		ArrayList<String> toSend = new ArrayList<>();

		toSend.add("<START>" + "\n");

		toSend.add("<COM>" + getComID() + "\n");
		toSend.add("<DEV>" + getDeviceID() + "\n");

		for (String d : getData()) {
			toSend.add(d);
		}

		toSend.add("<END>");

		return toSend.toArray(new String[toSend.size()]);
	}
}