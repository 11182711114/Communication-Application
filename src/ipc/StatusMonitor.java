package ipc;

import java.io.File;

import application.CommunicationApplication;
import log.Logger;

public class StatusMonitor implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private static final long SCAN_INTERVAL_IN_MS = 1000;

	public static final String ACTIVE_CODE = "ACTIVE";
	public static final String INACTIVE_CODE = "INACTIVE";
	public static final String SHUTDOWN_CODE = "SHUTDOWN";

	private boolean programActive;
	private File folderToMonitor;
	private boolean active;
	private CommunicationApplication ca;

	public StatusMonitor(File folderToMonitor, CommunicationApplication ca) {
		this.folderToMonitor = folderToMonitor;
		this.ca = ca;
	}

	@Override
	public void run() {
		log.info("Starting operation");
		active = true;
		while (active) {
			log.trace("Starting scan");
			for (File f : folderToMonitor.listFiles()) {
				switch (f.getName()) {
				case ACTIVE_CODE:
					if (ca.getStatus() != ACTIVE_CODE) {
						log.trace("New status: " + ACTIVE_CODE);
						ca.setStatus(ACTIVE_CODE);
					}
					break;
				case INACTIVE_CODE:
					if (ca.getStatus() != INACTIVE_CODE) {
						log.trace("New status: " + INACTIVE_CODE);
						ca.setStatus(INACTIVE_CODE);
					}
				case SHUTDOWN_CODE:
					if (ca.getStatus() != SHUTDOWN_CODE) {
						log.trace("New status: " + SHUTDOWN_CODE);
						ca.setStatus(SHUTDOWN_CODE);
					}
				}
			}
			try {
				Thread.sleep(SCAN_INTERVAL_IN_MS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isProgramActive() {
		return programActive;
	}
}
