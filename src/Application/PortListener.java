
package Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import Util.LogLevel;
import Util.Logger;

//OBS formatera!

public class PortListener implements Runnable {

	private Logger log = Util.Logger.getInstance();
	private String className = this.getClass().getName();

	private ChannelHandler HANDLER;
	private ServerSocket SOCKET;
	private boolean active;

	public PortListener(ChannelHandler h, ServerSocket s) {
		HANDLER = h;
		SOCKET = s;
	}

	@Override
	public void run() {
		log.debug("Starting port listener", System.currentTimeMillis(), className);
		active = true;
		while (active) {
			if (SOCKET == null)
				return;
			try {
				Socket newSocket = SOCKET.accept();
				log.debug("Passing conversation to new socket", System.currentTimeMillis(), className);
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