package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
	Socket iSocket;
	ServerSocket sSocket;
	
	public Connection(InetAddress adr,int port) {
		try {
			iSocket = new Socket(adr,port);
		} catch (IOException e) {e.printStackTrace();}
	}

	public Connection(int port) {
		try {
			sSocket = new ServerSocket(port);
			iSocket = sSocket.accept();
		} catch (IOException e) {e.printStackTrace();}
	}

	public void connect() {
		
	}
	public void listenServerSocket(){
		if(sSocket != null){
			try {
				sSocket.accept();
			} catch (IOException e) {e.printStackTrace();}
		}
		outputTestSocket();
	}
	private void outputTestSocket() {
		while(true){
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {e.printStackTrace();}
			
			String toPrint = receive();
			if(toPrint != null)
				System.out.println();
		}
	}

	public void send(String string) {
		try {
			PrintWriter output = new PrintWriter(iSocket.getOutputStream(),true);
			output.write(string);
		} catch (IOException e) {e.printStackTrace();}
	}
	public String receive(){
		String output = "";
		
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(iSocket.getInputStream()));
			output = input.readLine();
		} catch (IOException e) {e.printStackTrace();}
		
		return output;
	}

	public void close() {
		try{
			sSocket.close();
			iSocket.close();
		}catch(IOException e){e.printStackTrace();}
	}
}
