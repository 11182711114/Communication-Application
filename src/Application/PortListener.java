
package Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//OBS formatera!

public class PortListener implements Runnable {
	private ChannelHandler HANDLER;
	private ServerSocket SOCKET;
	private boolean active;

	public PortListener(ChannelHandler h, ServerSocket s) {
		HANDLER = h;
		SOCKET = s;
		active = true;
	}

	@Override
	public void run() {
			while(active){
			if(SOCKET == null)
				return;
			try {
				Socket newSocket = SOCKET.accept();
				HANDLER.passSocket(newSocket);
			} catch (IOException e) {e.printStackTrace();}
		}
	}
	public void stop(){
		active = false;
	}

}