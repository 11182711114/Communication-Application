package Application;

public class Reader implements InOutPuttable{
	
	private IO myIo;
	private int readerId = 1;
	public static int readerCount;
	
	
	public Reader(IO myIo){
		this.myIo=myIo;
		
		readerCount = readerId;
		readerId = readerId++;
	}

	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}

}
