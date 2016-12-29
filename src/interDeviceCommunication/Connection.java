
package interDeviceCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.DataPacket;
import application.InputDataPacket;
import application.OutputDataPacket;

//OBS formatera!

public class Connection implements Runnable {

	private Socket socket;
	private boolean run;
	private Channel channel;
	
	private util.Logger log = util.Logger.getInstance();
	private String nameForLog = this.getClass().getSimpleName();

	public Connection(Socket s) {
		socket = s;
		
		
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
				
		} catch (InterruptedException e) {
			e.printStackTrace();
			close();
		} catch (IOException e) {
			e.printStackTrace();
			close();
		}
	}

	public void changeSocket(Socket newSocket) {
		socket = newSocket;
	}

	public boolean send(OutputDataPacket packet) {
		try {
			BufferedWriter output = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
			String[] data = packet.toSend();
			
			for(String d : data)
			{
				output.write(d);
				output.flush();
			}
			
			output.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void read(Scanner scanner) 
	{
		if (scanner.hasNext()) 
		{
			InputDataPacket packet = new InputDataPacket();

			while (scanner.hasNext()) 
			{
				String input = scanner.nextLine();
				
				packet.parseData(input);
				
				if(input.equals("<END>"))
				{
					channel.inputPacket(packet);
					packet = new InputDataPacket();
				}
			}
		}
	}

	public void close() {
		run = false;
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}