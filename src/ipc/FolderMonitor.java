package ipc;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import application.ChannelHandler;
import log.Logger;

public class FolderMonitor implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private static final int SCAN_INTERVAL_IN_MS = 1000;

	private File parentDir;
	private Set<File> knownDirectories;
	private ChannelHandler cH;

	private boolean active = false;

	public FolderMonitor(File parent, Set<File> knownFiles, ChannelHandler cH) {
		parentDir = parent;
		knownDirectories = knownFiles;
		this.cH = cH;
	}

	@Override
	public void run() {
		active = true;
		log.info("Starting continuous operations");

		while (active) {
			scan();
			// readFiles();
			try {
				Thread.sleep(SCAN_INTERVAL_IN_MS);
			} catch (InterruptedException e) {
				log.error("interrupted");
				log.exception(e);
			}
		}

	}
	
	public void scan() {
		log.trace("Scanning files in: " + parentDir.getAbsolutePath());
		File[] filesTMP = parentDir.listFiles();

		for (File devIdFolder : filesTMP) {
			if (devIdFolder.isDirectory()) {
				log.trace("DeviceId folder found: " + devIdFolder.getAbsolutePath());
				for (File comIdFolder : devIdFolder.listFiles()) {
					if (comIdFolder.isDirectory()) {
						if (!knownDirectories.contains(comIdFolder)) {
							log.trace("New ComId folder found: " + comIdFolder.getAbsolutePath());
							try {
								if(cH.passComFolder(comIdFolder))
								{
									log.trace("Adding to known collection: " + comIdFolder.getAbsolutePath());
									knownDirectories.add(comIdFolder);
								}
							} catch (IOException e) {
								log.error("New folder was deleted while trying to make a new channel");
							}
						}
					}
				}
			}
		}
	}

	public void stop() {
		log.info("Stopping");
		active = false;
	}
}
