package application;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
	private PortListener sListener;
	private FolderMonitor fMon;
	private Discovery disc;
	private File monitorDir;

	public ChannelHandler(List<Channel> cons, File monitorDir, Discovery disc) {
		this.channels = cons;
		fMon = new FolderMonitor(monitorDir, new HashSet<File>(), this);
		this.monitorDir = monitorDir;
		this.disc = disc;
	}

	public ChannelHandler(List<Channel> cons, File monitorDir) {
		this.channels = cons;
		fMon = new FolderMonitor(monitorDir, new HashSet<File>(), this);
		this.monitorDir = monitorDir;
	}

	public void addAndStartChannel(Channel c) {
		log.debug("Attempting to start channel");
		new Thread(c).start();
		channels.add(c);
	}

	/**
	 * Making a new Channel from a passed socket
	 * 
	 * @param passedSocket
	 *            - the socket to pass
	 */
	public void passSocket(Socket passedSocket) {
		log.info("Making new channel based on passed socket");

		Connection conTmp = new Connection(passedSocket);
		Channel tmp = new Channel(conTmp, monitorDir);

		log.debug("Adding channel to channel chain");
		addAndStartChannel(tmp);
	}

	/**
	 * Creates a new Channel from a passed directory
	 * 
	 * @param comFolder
	 *            - the folder
	 * @throws IOException
	 *             when the folder does not exist
	 */
	public void passComFolder(File comFolder) throws IOException {
		log.info("Making new channel based on passed ComIdFolder");
		IO io = new IO(comFolder);

		log.debug("monitorDir: " + monitorDir + " comFolder: " + comFolder);
		String deviceId = DeviceIdExtractor.extractFromFolder(monitorDir, comFolder);
		log.debug("Extracting ip from: " + comFolder.getCanonicalPath() + " resulted in deviceId: " + deviceId);
		InetAddress ip = InetAddress.getByName(deviceId);
		Connection con = new Connection(new Socket(ip, sListener.getServerSocketPort()));

		Channel chan = new Channel(con, io);

		addAndStartChannel(chan);
	}

	/**
	 * Sets ChannelHandlers PortListener
	 * 
	 * @param pl
	 *            - The PortListener to set
	 */
	public void setPortListener(PortListener pl) {
		log.debug("Setting port listener to port: " + pl.getServerSocketPort());
		this.sListener = pl;
	}

	private void checkChannels() {

		while (true) {
			ArrayList<Channel> inActive = (ArrayList<Channel>) channels.stream().filter(c -> !c.returnActive())
					.collect(Collectors.toList());
			channels.removeAll(inActive);
			if (inActive.size() > 0) {
				log.debug(inActive.size() + " Channels removed");
			}

		}
	}

	public void start() {
		log.info("Starting ChannelHandler");
		new Thread(sListener).start();
		new Thread(fMon).start();
		if (disc != null) {
			new Thread(disc).start();
		}
		checkChannels();
	}

	public void fullStop() {
		log.info("Stopping");
		sListener.stop();
		for (Channel c : channels) {
			c.stop();
		}
	}
}
