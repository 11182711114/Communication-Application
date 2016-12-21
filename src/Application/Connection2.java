
package Application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//OBS formatera!

public class Connection2 implements Runnable
{
	
	private Socket socket;
	private Channel channel;
	
	private List<DataPacket> data;
	
	private boolean run;
	
	public Connection2(Socket s, Channel h)
	{
		socket = s;
		channel = h;
		data = new ArrayList<>();
	}
	
	@Override
	public void run() 
	{
		
			try 
			{
				Scanner input = new Scanner(socket.getInputStream());
				
				run = true;
				
				while(run)
				{
					read(input);
					Thread.sleep(100);
				}
				
				
				
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
				exitRun();
			} catch (IOException e) 
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
			
			output.write(s);
			output.flush();
			
			output.close();
			return true;
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	private void read(Scanner scanner)
	{
		while(scanner.hasNext())
		{
			InputDataPacket packet = new InputDataPacket();
			String input = scanner.nextLine();
			
			do
			{
				DataParser.parseData(input, packet);
			}
			while(!input.equals("<END>"));
		}
	}
}