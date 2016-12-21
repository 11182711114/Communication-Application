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
		log.Log("Making new channel based on passed socket", LogLevel.Debug, System.currentTimeMillis());
		
		Connection conTmp = new Connection(s);
		IO ioTmp = new IO();
		Channel tmp = new Channel(conTmp, ioTmp);
		tmp.run();
		
		log.Log("Adding channel to channel chain", LogLevel.Debug, System.currentTimeMillis());
		
		channelsSet.add(tmp);
		channels.add(tmp);		
	}
	public void setPortListener(PortListener pl){
		log.Log("Setting port listener to port: " + pl.getServerSocketPort(), LogLevel.Debug, System.currentTimeMillis());
		this.sListener = pl;
	}
	public void start(){
		log.Log("Starting ChannelHandler", LogLevel.Debug, System.currentTimeMillis());
		
		active = true;
		sListener.run();
		
	}
}
