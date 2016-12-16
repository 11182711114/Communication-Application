
package Application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

//OBS formatera!

public class Connection2 implements Runnable 
{
	private Socket socket;
	private Channel channel;
	
	private boolean run;
	
	public Connection2(Socket s, Channel h)
	{
		socket = s;
		channel = h;
	}
	
	@Override
	public void run() 
	{
		run = true;
		do
		{
			try 
			{
				DataInputStream input = new DataInputStream(socket.getInputStream());
				
				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				exitRun();
			}
		}
		while(run);
	}
	
	public void changeSocket(Socket newSocket)
	{
		socket = newSocket;
	}
	
	public void exitRun()
	{
		run = false;
	}
	
	public boolean send(String s)
	{
		try 
		{
			DataOutputStream output = new DataOutputStream(socket.getOutputStream());
			
			output.writeUTF(s);
			output.flush();
			
			output.close();
			return true;
		} 
		catch (IOException e)
		{
			//Tänk på felhantering!
			e.printStackTrace();
			return false;
		}
	}
	
	public void receive()
	{
		
	}
}