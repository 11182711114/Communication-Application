package application;



import java.io.File;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import discovery.Discovery;
import interDeviceCommunication.Channel;
import interDeviceCommunication.Connection;
import interDeviceCommunication.PortListener;
import ipc.FolderMonitor;
import ipc.IO;

public class ChannelHandler {
	private util.Logger log = util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();

	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;
	private FolderMonitor fMon;
	private Discovery disc;

	public ChannelHandler(Set<Channel> channelsSet, List<Channel> cons, File monitorDir) {
		this.channels = cons;
		this.channelsSet = channelsSet;
		fMon = new FolderMonitor(monitorDir);
	}
	public ChannelHandler(Set<Channel> channelsSet, List<Channel> cons, File monitorDir,Discovery disc) {
		this.channels = cons;
		this.channelsSet = channelsSet;
		fMon = new FolderMonitor(monitorDir);
		this.disc = disc;
	}
	
	public void addChannel(Channel c) {
		new Thread(c).start();
		channels.add(c);
		channelsSet.add(c);
	}

	public void passSocket(Socket s) {
		log.info("Making new channel based on passed socket", nameForLog);

		Connection conTmp = new Connection(s);
		IO ioTmp = new IO(" ");
		Channel tmp = new Channel(conTmp, ioTmp);

		log.debug("Adding channel to channel chain", nameForLog);
		addChannel(tmp);
	}

	public void setPortListener(PortListener pl) {
		log.debug("Setting port listener to port: " + pl.getServerSocketPort(), nameForLog);
		this.sListener = pl;
	}

	public void start() {
		log.info("Starting ChannelHandler", nameForLog);
		new Thread(sListener).start();
		new Thread(fMon).start();
		if(disc != null)
			new Thread(disc).start();
	}
	public void fullStop(){
		log.info("Stopping", nameForLog);
		sListener.stop();
		for(Channel c : channels){
			c.stop();
		}
	}
}
