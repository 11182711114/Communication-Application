package test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import interDeviceCommunication.Channel;
import interDeviceCommunication.Connection;
import ipc.IO;
import util.FileUtil;

public class TestCh_Con_IO {

	public void runTest(){
		
		try {
			
			for(File f : new File("./files/127.0.0.1/testCom/Output/Sent/").listFiles())
			{
				f.delete();
			}
			FileUtil.writeToFile("kiss",new File("./files/127.0.0.1/testCom/Output/Send/bajs.txt" ));
			
			ServerSocket ss = new ServerSocket(8080);
			Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 8080);
			IO io = new IO("./files/127.0.0.1/testCom/");
			Connection con = new Connection(s);
			Channel ch= new Channel(con, io);
			
			
			new Thread(){
				@Override 
				public void run(){
					try {
						Socket s2 = ss.accept();
						Connection con2 = new Connection(s2);
						Channel ch2 = new Channel(con2, new File("./files/127.0.0.1/"));
						
						ch2.run();
						ss.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}.start();
			new Thread(ch).start();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
