package interDeviceCommunication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;
import ipc.IO;
import log.Logger;

public class Channel implements Runnable {
	private boolean active;

	private Connection con;
	private IO inOut;
	private File workingDirectory;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	/**
	 * Handles Connections and IO operations.
	 * 
	 * @param Con
	 *            - connection between two devices
	 * @param inOut
	 *            - IPC between two processes
	 */
	public Channel(Connection con, IO inOut) {
		this.con = con;
		this.inOut = inOut;
	}

	public Channel(Connection con, File workingDirectory) {
		this.con = con;
		this.workingDirectory = new File(workingDirectory.getAbsolutePath() + File.separator + con.deviceId());
	}

	@Override
	public void run() {
		log.info("Starting channel");
		active = true;
		con.setChannel(this);
		new Thread(con).start();
		while (active) {
			try {
				active = con.checkConnection();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (inOut != null) {
				log.trace("checking for output");
				if (inOut.checkForOutput()) {

					try {
						OutputDataPacket[] data = inOut.getOutput();
						con.send(data);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}
	}

	public boolean getActive() {
		return active;
	}

	public void inputPacket(InputDataPacket packet) {
		if (inOut == null) {
			inputComID(packet.getComID());
		}
		inOut.handleInput(packet);
	}

	public void inputComID(String comID) {
		inOut = new IO(new File(workingDirectory.getAbsolutePath() + "/" + comID + "/"));
	}

	public void stop() {
		log.info("Stopping");
		active = false;
		con.close();
	}
}
