package ipc;

import java.io.File;

import log.Logger;

public class StatusMonitor implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private static final long SCAN_INTERVAL_IN_MS = 1000;

	public static final String ACTIVE_CODE = "ACTIVE";
	public static final String INACTIVE_CODE = "INACTIVE";

	private boolean programActive;
	private File folderToMonitor;
	private boolean active;

	public StatusMonitor(File folderToMonitor) {
		this.folderToMonitor = folderToMonitor;
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
					if (!programActive == true) {
						log.trace("New status found, setting programActive: true");
						programActive = true;
					}
					break;
				case INACTIVE_CODE:
					if (!programActive == false) {
						log.trace("New status found, setting programActive: false");
						programActive = false;
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
