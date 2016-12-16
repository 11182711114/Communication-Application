package Application;

import java.util.List;

public class ChannelHandler {
	
	private List<Connection> cons;
	private PortListener sListener;
	
	public ChannelHandler(List<Connection> cons,PortListener sListener){
		this.cons = cons;
		this.sListener = sListener;
	}
	

}
