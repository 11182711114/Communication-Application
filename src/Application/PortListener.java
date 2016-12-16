
package Application;

import java.net.ServerSocket;

public class PortListener 
{
	private final ChannelHandler HANDLER;
	private final ServerSocket SOCKET;
	
	public PortListener(ChannelHandler h, ServerSocket s)
	{
		HANDLER = h;
		SOCKET = s;
	}
}