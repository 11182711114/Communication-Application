
package Application;

import java.net.ServerSocket;

//OBS formatera!

public class PortListener implements Runnable
{
	private final ChannelHandler HANDLER;
	private final ServerSocket SOCKET;
	
	public PortListener(ChannelHandler h, ServerSocket s)
	{
		HANDLER = h;
		SOCKET = s;
	}

	@Override
	public void run() 
	{
		 
	}
	
}