package test;

import application.InputDataPacket;
import ipc.IO;

public class testProgram {

	public static void main(String[] args) {
		IO io = new IO();
		InputDataPacket idp = new InputDataPacket();

		idp.setComID("testCom");
		idp.setDeviceID("testDevice");
		idp.saveData("hej");
		idp.saveData("hej2");
		idp.saveData("hej3");

		io.handle(idp);
	}

}
