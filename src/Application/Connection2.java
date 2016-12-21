
package Application;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//OBS formatera!

public class Connection2 implements Runnable {
	
	private Socket socket;
	private Channel channel;

	private List<DataPacket> data;
	
	private boolean run;

	public Connection2(Socket s, Channel h) {
		socket = s;
		channel = h;
		data = new ArrayList<>();
	}
	public Connection2(Socket s){
		socket = s;
	}

	@Override
	public void run() {

		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			run = true;
			checkForInput(input);

		} catch (IOException e) {
			e.printStackTrace();
			exitRun();
		}
	public void run() 
	{
		
			try 
			{
				Scanner input = new Scanner(socket.getInputStream());
				
				run = true;
				
				do
				{
					read(input);
					Thread.sleep(100);
				}
				while(run);
				
				
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
	public void run() 
	{
		
			try 
			{
				Scanner input = new Scanner(socket.getInputStream());
				
				run = true;
				
				do
				{
					read(input);
					Thread.sleep(100);
				}
				while(run);
				
				
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

	public void changeSocket(Socket newSocket) {
		socket = newSocket;
	}

	public void exitRun() {
		run = false;
	}

	public boolean send(String s) {
		try {
			BufferedWriter output = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));

			output.write(s);
			;
			output.flush();

			output.close();
			return true;
		} catch (IOException e) {
			// Tänk på felhantering!
		} 
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	private void checkForInput(BufferedReader reader) {
		try {
			do {
				if (reader.ready()) {
					handleInput(reader);
				}
				Thread.sleep(100);
			} while (run);
		} catch (InterruptedException e) {
			e.printStackTrace();
			run = false;
		} catch (IOException e) {
			e.printStackTrace();
			run = false;
	
	private void read(Scanner scanner)
	{
		while(scanner.hasNext())
		{
			
		}
	}

	public void handleInput(BufferedReader reader) {

	}
	public void setChannel(Channel c){
		this.channel = c;
	}
}