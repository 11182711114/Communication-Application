package dataPacket;

public class InputDataPacket extends DataPacket {
	@Override
	public void parseData(String line) {
		int start = line.indexOf("<");
		int end = line.indexOf(">") + 1;
		if (start == -1 || end == -1)
			return;

		String markup = line.substring(start, end);
		String data = line.substring(line.indexOf(">") + 1).trim();

		switch (markup) {
		case "<COM>":
			setComID(data);
			break;
		case "<DEV>":
			setDeviceID(data);
			break;
		case "<DATA>":
			saveData(data);
			break;
		}
	}
}