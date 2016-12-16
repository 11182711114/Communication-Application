package Application;

import java.util.List;
import java.util.Set;

public class ChannelHandler {
	
	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;
	
	public ChannelHandler(Set<Channel> channelsSet,List<Channel> cons,PortListener sListener){
		this.channels = cons;
		this.sListener = sListener;
		this.channelsSet = channelsSet;
	}
	public void addChannel(Channel c){
		c.startChannel();
		channels.add(c);		
	}
}
