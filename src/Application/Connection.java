package Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
	Socket iSocket;
	
	public Connection(InetAddress adr,int port) throws IOException {
		iSocket = new Socket(adr,port);
	}

	public void connect() {
		
	}

	public void send(String string) {
		try {
			PrintWriter output = new PrintWriter(iSocket.getOutputStream(),true);
			output.write(string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public String receive(){
		String output = "";
		
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(iSocket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return output;
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}
}
