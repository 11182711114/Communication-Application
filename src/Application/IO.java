package Application;
import java.util.ArrayList;

public class IO {

	ArrayList<Writer>  writerCollection = new ArrayList<>();	
	ArrayList<Reader>  readerCollection = new ArrayList<>();
	
	
	
	
	public IO(){
		
	}
	
	public void createWriter(){
		Writer writer = new Writer();
		writerCollection.add(writer);
	}
	public void createReader(){
		Reader reader = new Reader();
		readerCollection.add(reader);	
	}
}
