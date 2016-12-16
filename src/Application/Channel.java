package Application;

public class Channel implements Comparable<Channel>{
	private String comID;
	
	private Connection2 con;
	private IO inOut;
	
	/**
	 * Handles Connections and IO operations.
	 * @param  Con connection between two devices
	 * @param  inOut IPC between two processes
	 */
	public Channel(Connection2 con, IO inOut){
		this.con = con;
		this.inOut = inOut;		
	}
	public void startChannel(){
		con.run();
	}
	@Override
	public int compareTo(Channel c) {
		return comID.equals(c.getComID()) ? 1 : 0;
	}
	public String getComID(){
		return comID;
	}
}
