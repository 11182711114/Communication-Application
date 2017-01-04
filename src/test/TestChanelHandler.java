package test;

import java.io.File;
import java.io.IOException;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashSet;

import application.ChannelHandler;
import interDeviceCommunication.Channel;
import interDeviceCommunication.PortListener;

public class TestChanelHandler {

	public void runTest() {
		ChannelHandler handler = new ChannelHandler(new HashSet<Channel>(), new ArrayList<Channel>(),
				new File("./files/"));
		try {
			PortListener pL = createPortListener(handler);
			handler.setPortListener(pL);
			handler.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PortListener createPortListener(ChannelHandler h) throws IOException {
		ServerSocket ss = new ServerSocket(8080);

		return new PortListener(h, ss);
	}
}