package Application;

import java.net.Socket;
import java.util.List;
import java.util.Set;

import Util.LogLevel;

public class ChannelHandler {
	private Util.Logger log= Util.Logger.getInstance();
	
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
		log.Log("Starting", LogLevel.Debug, System.currentTimeMillis());
		
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
