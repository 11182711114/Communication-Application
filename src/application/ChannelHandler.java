package application;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import discovery.Discovery;
import interDeviceCommunication.Channel;
import interDeviceCommunication.Connection;
import interDeviceCommunication.PortListener;
import ipc.FolderMonitor;
import ipc.IO;
import log.Logger;
import util.DeviceIdExtractor;

public class ChannelHandler {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private List<Channel> channels;
	private Set<Channel> channelsSet;
	private PortListener sListener;
	private FolderMonitor fMon;
	private Discovery disc;
	private File monitorDir;

	public ChannelHandler(Set<Channel> channelsSet, List<Channel> cons, File monitorDir) {
		this.channels = cons;
		this.channelsSet = channelsSet;
		this.monitorDir = monitorDir;
		fMon = new FolderMonitor(monitorDir, new HashSet<File>(),this);
	}

	public ChannelHandler(Set<Channel> channelsSet, List<Channel> cons, File monitorDir, Discovery disc) {
		this.channels = cons;
		this.channelsSet = channelsSet;
		fMon = new FolderMonitor(monitorDir, new HashSet<File>(),this);
		this.disc = disc;
	}

	public void addAndStartChannel(Channel c) {
		new Thread(c).start();
		channels.add(c);
		channelsSet.add(c);
	}

	public void passSocket(Socket s) {
		log.info("Making new channel based on passed socket");

		Connection conTmp = new Connection(s);
		Channel tmp = new Channel(conTmp, monitorDir);

		log.debug("Adding channel to channel chain");
		addAndStartChannel(tmp);
	}
	
	/** Creates a new Channel from a passed directory
	 * @param comFolder - the folder
	 * @throws IOException when the folder does not exist
	 */
	public void passComFolder(File comFolder) throws IOException{
		log.info("Making new channel based on passed ComIdFolder");
		IO io = new IO(comFolder);

		log.debug("monitorDir: " + monitorDir + " comFolder: " + comFolder);
		String deviceId = DeviceIdExtractor.extractFromFolder(monitorDir, comFolder);
		log.debug("Extracting ip from: "+ comFolder.getCanonicalPath() + " resulted in deviceId: " + deviceId);
		InetAddress ip = InetAddress.getByName(deviceId);
		Connection con = new Connection(new Socket(ip,sListener.getServerSocketPort()));

		Channel chan = new Channel(con,io);
		
		addAndStartChannel(chan);
	}

	/** Sets ChannelHandlers PortListener
	 * @param pl - The PortListener to set
	 */
	public void setPortListener(PortListener pl) {
		log.debug("Setting port listener to port: " + pl.getServerSocketPort());
		this.sListener = pl;
	}

	public void start() {
		log.info("Starting ChannelHandler");
		new Thread(sListener).start();
		new Thread(fMon).start();
		if (disc != null)
			new Thread(disc).start();
	}

	public void fullStop() {
		log.info("Stopping");
		sListener.stop();
		for (Channel c : channels) {
			c.stop();
		}
	}
}
