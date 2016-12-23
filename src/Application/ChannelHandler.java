package Application;

import java.net.Socket;
import java.util.List;
import java.util.Set;

import Util.LogLevel;

public class ChannelHandler {
	private Util.Logger log = Util.Logger.getInstance();
	private String className = this.getClass().getName();

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
	}

	public void passSocket(Socket s) {
		log.debug("Making new channel based on passed socket", System.currentTimeMillis(), className);

		Connection conTmp = new Connection(s);
		IO ioTmp = new IO();
		Channel tmp = new Channel(conTmp, ioTmp);
		tmp.run();

		log.debug("Adding channel to channel chain", System.currentTimeMillis(), className);

		channelsSet.add(tmp);
		channels.add(tmp);
	}

	public void setPortListener(PortListener pl) {
		log.debug("Setting port listener to port: " + pl.getServerSocketPort(), System.currentTimeMillis(), className);
		this.sListener = pl;
	}

	public void start() {
		log.debug("Starting ChannelHandler", System.currentTimeMillis(), className);

		new Thread(sListener).start();
	}
}
