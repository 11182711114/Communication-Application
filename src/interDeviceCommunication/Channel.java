package interDeviceCommunication;

import java.io.FileNotFoundException;

import application.InputDataPacket;
import application.OutputDataPacket;
import ipc.IO;
import util.Logger;

public class Channel implements Comparable<Channel>, Runnable {

	// FIXME FOR TESTING ONLY
	private static int ID = 0;

	private synchronized int getID() {
		return ID;
	}

	private synchronized void incrementID() {
		ID++;
	}

	private String comID;
	private boolean active;

	private Connection con;
		@SuppressWarnings("unused")
	private IO inOut;

	private Logger log = Logger.getLogger(this.getClass().getSimpleName() + "@" + comID);

	/**
	 * Handles Connections and IO operations.
	 * 
	 * @param Con
	 *            connection between two devices
	 * @param inOut
	 *            IPC between two processes
	 */
	public Channel(Connection con, IO inOut) {
		// FIXME FOR TESTING ONLY
		comID = Integer.toString(getID());
		incrementID();

		this.con = con;
		this.inOut = inOut;
	}

	@Override
	public void run() {
		log.info("Starting channel");
		active = true;
		new Thread(con).start();
		while (active) {
			
			if(inOut.checkForOutput("testDevice")){
				
				try {
					OutputDataPacket[] data= inOut.sendDataPackets("testDevice");
					con.send(data);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// FIXME Do something check for datatosend
		}
	}
	
	public void inputPacket(InputDataPacket packet)
	{
		inOut.handle(packet);
	}

	@Override
	public int compareTo(Channel c) {
		return comID.equals(c.getComID()) ? 1 : 0;
	}

	public String getComID() {
		return comID;
	}

	public void exit() {
		con.close();
	}
	public void stop(){
		log.info("Stopping");
		active = false;
	}
}
