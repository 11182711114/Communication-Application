
package Application;

import java.net.Socket;

public class Connection2 implements Runnable 
{
	private Socket socket;
	private Channel channel;
	
	public Connection2(Socket s, Channel h)
	{
		socket = s;
		channel = h;
	}
	
	@Override
	public void run() 
	{
		
	}
}
