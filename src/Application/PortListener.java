
package Application;

import java.net.ServerSocket;

//OBS formatera!

public class PortListener implements Runnable {
	private ChannelHandler HANDLER;
	private ServerSocket SOCKET;

	public PortListener(ChannelHandler h, ServerSocket s) {
		HANDLER = h;
		SOCKET = s;
	}

	@Override
	public void run() {
		
	}

}