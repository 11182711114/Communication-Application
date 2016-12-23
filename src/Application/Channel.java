package Application;

public class Channel implements Comparable<Channel>, Runnable {
	private String comID;
	private boolean running;

	private Connection con;
	private IO inOut;

	/**
	 * Handles Connections and IO operations.
	 * 
	 * @param Con
	 *            connection between two devices
	 * @param inOut
	 *            IPC between two processes
	 */
	public Channel(Connection con, IO inOut) {
		this.con = con;
		this.inOut = inOut;
	}

	@Override
	public void run() {
		running = true;
		con.run();
		while (running) {

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
		// io.close();
	}
}
