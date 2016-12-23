
package Application;

public class InputDataPacket extends DataPacket {
	private String deviceID;

	private void setDeviceID(String ID) {
		deviceID = ID;
	}

	public String getDeviceID() {
		return deviceID;
	}

	@Override
	public void parseData(String line) {
		String markup = line.substring(line.indexOf("<"), line.indexOf(">") + 1);
		String data = line.substring(line.indexOf(">") + 1).trim();

		switch (markup) {
		case "<COM>":
			setComID(data);
		case "<DEV>":
			setDeviceID(data);
		case "<DATA>":
			saveData(data);
		case "<CHECK>":
			checkSum(data);
		}

	}
}