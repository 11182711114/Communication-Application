package Application;

import java.util.List;
import java.util.Set;

public class ChannelHandler {
	
	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;
	
	public ChannelHandler(Set<Channel> channelsSet,List<Channel> cons){
		this.channels = cons;
		this.channelsSet = channelsSet;
	}
	public void addChannel(Channel c){
		c.run();
		channels.add(c);		
	}
	public void setPortListener(PortListener pl){
		this.sListener = pl;
	}
	public void continuousOperation() {
		
	}
}
