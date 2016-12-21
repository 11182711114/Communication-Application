
package Application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
		
			try 
			{
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				run = true;
				checkForInput(input);
				
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				exitRun();
			}
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
			BufferedWriter output = new BufferedWriter(new PrintWriter(socket.getOutputStream(),true));
			
			output.write(s);;
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
	
	private void checkForInput(BufferedReader reader)
	{
		try
		{
			do
			{
				if(reader.ready())
				{
					handleInput(reader);
				}
				Thread.sleep(100);
			} 
			while(run);
		}
		catch(InterruptedException e)
		{
			e.printStackTrace();
			run = false;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			run = false;
		}
	}
	
	public void handleInput(BufferedReader reader)
	{
		
	}
}