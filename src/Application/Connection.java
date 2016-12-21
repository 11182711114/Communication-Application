
package Application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//OBS formatera!

public class Connection implements Runnable
{
	
	private Socket socket;
	
	private List<DataPacket> data;
	
	private boolean run;
	
	public Connection(Socket s)
	{
		socket = s;
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
				close();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				close();
			}
	}
	
	public void changeSocket(Socket newSocket)
	{
		socket = newSocket;
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
		if(scanner.hasNext())
		{
			InputDataPacket packet = null;
			
			while(scanner.hasNext())
			{
				String input = scanner.nextLine();
				
				if(input.equals("<START>"))
				{
					packet = new InputDataPacket();
					data.add(packet);
				}
				packet.parseData(input);
			}
		}
	}
	public void close(){
		run = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}