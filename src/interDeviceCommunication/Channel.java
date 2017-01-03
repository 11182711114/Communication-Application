package interDeviceCommunication;

import java.io.File;
import java.io.FileNotFoundException;

import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;
import ipc.IO;
import log.Logger;

public class Channel implements Comparable<Channel>, Runnable {

	private String comID;
	private boolean active;

	private Connection con;
	private IO inOut;
	private File workingDirectory;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName() + "@" + comID);

	/**
	 * Handles Connections and IO operations.
	 * @param Con - connection between two devices
	 * @param inOut - IPC between two processes
	 */
	public Channel(Connection con, IO inOut) {
		this.con = con;
		this.inOut = inOut;
	}
	
	public Channel(Connection con, File workingDirectory) {
		this.con = con;
		this.workingDirectory = workingDirectory;
	}
	
	@Override
	public void run() {
		log.info("Starting channel");
		active = true;
		con.setChannel(this);
		new Thread(con).start();
		while (active) {
			if(inOut != null){
				log.trace("checking for output");
				String deviceId = con.deviceId();
				if(inOut.checkForOutput()){
					
					try {
				
						OutputDataPacket[] data= inOut.sendDataPackets();
						con.send(data);

					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Do something check for datatosend
				
			}
		}
	}

	public void inputPacket(InputDataPacket packet) {
		if(inOut == null){
			comID = packet.getComID();
			inOut = new IO(workingDirectory.getAbsolutePath() + "/" +comID + "/");
		}
		inOut.handle(packet);
	}

	@Override
	public int compareTo(Channel c) {
		return comID.equals(c.getComID()) ? 1 : 0;
	}

	public String getComID() {
		return comID;
	}
	
	public void setComID(String comID){
		this.comID = comID;
	}

	public void exit() {
		con.close();
	}

	public void stop() {
		log.info("Stopping");
		active = false;
	}
}
