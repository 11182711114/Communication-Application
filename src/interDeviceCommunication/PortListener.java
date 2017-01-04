
package interDeviceCommunication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import application.ChannelHandler;
import log.Logger;

public class PortListener implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private ChannelHandler handler;
	private ServerSocket serverSocket;
	private boolean active;

	public PortListener(ChannelHandler h, ServerSocket s) {
		handler = h;
		serverSocket = s;
	}

	@Override
	public void run() {
		log.info("Starting port listener");
		active = true;
			while (active) {
				if (serverSocket == null)
					return;
				try {
					Socket newSocket = serverSocket.accept();
					log.debug("Passing conversation to new socket");
					handler.passSocket(newSocket);
				} catch (IOException e) {
					log.exception(e);
				}
			}
	}
	

	public void stop() {
		log.info("Stopping");
		active = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			log.debug("Socket was closed during run");
		}
	}

	public int getServerSocketPort() {
		return serverSocket.getLocalPort();
	}

}