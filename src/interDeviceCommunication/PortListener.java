
package interDeviceCommunication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import application.ChannelHandler;
import util.Logger;

//OBS formatera!

public class PortListener implements Runnable {
	private Logger log = Logger.getLogger(this.getClass().getSimpleName());

	private ChannelHandler HANDLER;
	private ServerSocket SOCKET;
	private boolean active;

	public PortListener(ChannelHandler h, ServerSocket s) {
		HANDLER = h;
		SOCKET = s;
	}

	@Override
	public void run() {
		log.info("Starting port listener");
		active = true;
		while (active) {
			if (SOCKET == null)
				return;
			try {
				Socket newSocket = SOCKET.accept();
				log.debug("Passing conversation to new socket");
				HANDLER.passSocket(newSocket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		active = false;
	}

	public int getServerSocketPort() {
		return SOCKET.getLocalPort();
	}

}