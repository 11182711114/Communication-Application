package Application;
import java.util.ArrayList;

public class IO implements Runnable{

	ArrayList<Writer>  writerCollection = new ArrayList<>();	
	ArrayList<Reader>  readerCollection = new ArrayList<>();
	
	
	private int id = 1; 
	public static int idCount;
	private Channel myChannel;
	
	
	public IO(Channel channel){
		myChannel = channel;
		idCount = id;
		this.id =id++;
		
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
