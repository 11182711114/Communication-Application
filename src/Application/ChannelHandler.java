package Application;

import java.net.Socket;
import java.util.List;
import java.util.Set;

public class ChannelHandler {
	
	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;
	
	private boolean active = false;
	
	public ChannelHandler(Set<Channel> channelsSet,List<Channel> cons){
		this.channels = cons;
		this.channelsSet = channelsSet;
	}
	public void addChannel(Channel c){
		c.run();
		channels.add(c);
	}
	public void passSocket(Socket s){
		Connection2 conTmp = new Connection2(s);
		IO ioTmp = new IO();
		Channel tmp = new Channel(conTmp, ioTmp);
		ioTmp.setChannel(tmp);
		
		tmp.run();
		channelsSet.add(tmp);
		channels.add(tmp);		
	}
	public void setPortListener(PortListener pl){
		this.sListener = pl;
	}
	public void start(){
		active = true;
		sListener.run();
		
		while(active){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
