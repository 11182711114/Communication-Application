package Application;

public class Writer implements InOutPuttable{

	private IO myIo;
	private int writerId = 1;
	public static int writerCount;
	
	public Writer(IO myIo){
		this.myIo=myIo;
		
		writerCount = writerId;
		writerId = writerId++;
		
	}
	
	@Override
	public void write() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read() {
		// TODO Auto-generated method stub
		
	}
	
	public IO getIo(){
		return myIo;
	}
	
	@Override
	public int getIOId() {
		// TODO Auto-generated method stub
		return writerId;
	}

}
