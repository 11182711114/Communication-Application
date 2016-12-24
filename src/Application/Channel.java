package Application;

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
	
	private Util.Logger log;
	private String nameForLog = this.getClass().getSimpleName() + "@" + comID;

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
		log.info("Starting channel", nameForLog);
		active = true;
		new Thread(con).start();
		while (active) {
			// FIXME Do something
		}
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
		active = false;
	}
}
