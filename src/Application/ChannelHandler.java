package Application;

import java.net.Socket;
import java.util.List;
import java.util.Set;

public class ChannelHandler {
	private Util.Logger log = Util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();

	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;

	public ChannelHandler(Set<Channel> channelsSet, List<Channel> cons) {
		this.channels = cons;
		this.channelsSet = channelsSet;
	}

	public void addChannel(Channel c) {
		new Thread(c).start();
		channels.add(c);
		channelsSet.add(c);
	}

	public void passSocket(Socket s) {
		log.info("Making new channel based on passed socket", nameForLog);

		Connection conTmp = new Connection(s);
		IO ioTmp = new IO();
		Channel tmp = new Channel(conTmp, ioTmp);

		log.debug("Adding channel to channel chain", nameForLog);
		addChannel(tmp);
	}

	public void setPortListener(PortListener pl) {
		log.debug("Setting port listener to port: " + pl.getServerSocketPort(), nameForLog);
		this.sListener = pl;
	}

	public void start() {
		log.debug("Starting ChannelHandler", nameForLog);
		new Thread(sListener).start();
	}
	public void fullStop(){
		sListener.stop();
		for(Channel c : channels){
			c.stop();
		}
	}
}
