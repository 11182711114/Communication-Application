package Application;

import java.util.List;

public abstract class DataPacket implements DataParser {
	private String comID;
	private List<String> data;

	public void setComID(String comID) {
		this.comID = comID;
	}

	public String getComID() {
		return comID;
	}

	public void saveData(String d) {
		data.add(d);
	}

	public List<String> getData() {
		return data;
	}

	public void checkSum(String checkSum) {
		System.out.println("DO STUFF HERE (cheksum)");
	}
}