
package interDeviceCommunication;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataPacket.InputDataPacket;
import dataPacket.OutputDataPacket;
import log.Logger;

//OBS formatera!

public class Connection implements Runnable {

	private Socket socket;
	private boolean run;
	private Channel channel;
	
	private String connectionType;
	
	
	private Logger log = Logger.getLogger(this.getClass().getSimpleName() + "@" + channel.getComID());

	public Connection(Socket s) {
		socket = s;
	}

	@Override
	public void run() {
		try {
			Scanner input = new Scanner(socket.getInputStream());

			run = true;

			while (run) {
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

	public void send(OutputDataPacket[] packets) {
		try {
			BufferedWriter output = new BufferedWriter(new PrintWriter(socket.getOutputStream(), true));
			for (OutputDataPacket p : packets) {
				String[] data = p.toSend();
				for (String d : data) {
					output.write(d);
					output.flush();
				}

			}

			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void read(Scanner scanner) {
		if (scanner.hasNext()) {
			InputDataPacket packet = new InputDataPacket();

			while (scanner.hasNext()) {
				String input = scanner.nextLine();

				packet.parseData(input);
				
				Pattern p = Pattern.compile("(\\<[a-z]{1}\\>)");
				Matcher m = p.matcher(input);
				if (m.find() && m.group()=="<TYPE>"){
					String type = input.substring(m.end());
					log.trace("Setting connectionType to: " + type);
					setConnectionType(type);
				}
				
				if (input.equals("<END>")) {
					log.trace("End tag found");
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

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}
}