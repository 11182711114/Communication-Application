package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
	Socket iSocket;
	ServerSocket sSocket;
	
	
	public Connection(Socket s) {
		iSocket = s;
	}

	public Connection(ServerSocket s) {
			sSocket = s;
	}

	public void connect() {
		
	}
	public void listenServerSocket(){
		if(sSocket != null){
			try {
				System.out.println("Listening on port: " + sSocket.getLocalPort());
				iSocket = sSocket.accept();
			} catch (IOException e) {e.printStackTrace();}
		}
		outputTestSocket();
	}
	private void outputTestSocket() {
		while(true){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {e.printStackTrace();}
			String toPrint = receive();
			System.out.println(toPrint);
		}
	}

	public void send(String string) {
		try {
			PrintWriter output = new PrintWriter(iSocket.getOutputStream(),true);
			output.println(string);
		} catch (IOException e) {e.printStackTrace();}
	}
	public String receive(){
		String output = "";
		
		try {
			if(iSocket == null)
				System.out.println("Null inputstream!");
			BufferedReader input = new BufferedReader(new InputStreamReader(iSocket.getInputStream()));
			System.out.println("Waiting for data on socket:" + iSocket.getLocalPort());
			output = input.readLine();
		} catch (IOException e) {e.printStackTrace();}
		
		return output;
	}

	public void close() {
		try{
			if(sSocket != null)
				sSocket.close();
			if(iSocket != null)
				iSocket.close();
		}catch(IOException e){e.printStackTrace();}
	}
}
