package interDeviceCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;
import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;
import log.Logger;

//OBS formatera!

public class Connection implements Runnable {

	private Socket socket;
	private int port;
	private InetAddress ip;
	private boolean run;
	private Channel channel;

	
	private Logger log;

	public Connection(Socket s) {
		socket = s;
	}
	
	public void setChannel(Channel channel){
		this.channel = channel;
		log = Logger.getLogger(this.getClass().getSimpleName() + "@" + channel.getComID());
	}

	@Override
	public void run() {			
			
		log.info("Starting connection");
		try {			
			Scanner input = new Scanner(socket.getInputStream());

			run = true;

			while (run) {
				read(input);
				Thread.sleep(1000);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeSocket(Socket newSocket) {
		socket = newSocket;
	}
	
	public String deviceId(){
		return socket.getInetAddress().getHostAddress();
	}

	public void send(OutputDataPacket[] packets) {
		try {
			log.debug("Checking if socket is conneced before writing: " + socket.isConnected());
//			if(!socket.isConnected())
//				socket.connect(new InetSocketAddress(ip,port));
				BufferedWriter output = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
			
			for (OutputDataPacket p : packets) {
				String[] data = p.toSend();
				
				for (String d : data) {
					log.debug("writeOutput " + d);
					System.out.println(d);
					output.write(d);
				}
				output.flush();
			}
		} catch (IOException e) {
			log.exception(e);
		}
	}

	private void read(Scanner scanner) {
		if (scanner.hasNext()) {
			
			InputDataPacket packet = new InputDataPacket();

			while (scanner.hasNext()) {
				String input = scanner.nextLine();
				System.out.println(input);
				log.debug("readInput " + input);
				packet.parseData(input);
				
				if (input.equals("<END>")) {
					log.trace("End tag found");
					channel.setComID(packet.getComID());
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
	public void setPort(int port) {
		this.port = port;		
	}
	public void setIp(InetAddress ip) {
		this.ip = ip;
		
	}
}