package test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

import application.ChannelHandler;
import interDeviceCommunication.Channel;
import interDeviceCommunication.PortListener;

public class TestChanelHandler 
{
	
	
	public void runTest()
	{
		ChannelHandler handler = new ChannelHandler(new HashSet<Channel>(), new ArrayList<Channel>(), new File("./files/" ));
		try 
		{
			PortListener pL = createPortListener(handler);
			new Thread(pL).start();
			handler.setPortListener(pL);
			File f = new File("./files/127.0.0.1/testCom/" );
			handler.passComFolder(f);
			
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	private PortListener createPortListener(ChannelHandler h) throws IOException
	{
		ServerSocket ss = new ServerSocket(8080);
		
		return new PortListener(h, ss);
	}
}