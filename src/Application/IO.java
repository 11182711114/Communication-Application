package Application;

import java.util.ArrayList;

public class IO implements Runnable {

	ArrayList<Writer> writerCollection = new ArrayList<>();
	ArrayList<Reader> readerCollection = new ArrayList<>();

	private int id = 1;
	public static int idCount;

	public IO() {
		idCount = id;
		id = id++;

	}

	public void createWriter() {
		Writer writer = new Writer();
		writerCollection.add(writer);
	}

	public void createReader() {
		Reader reader = new Reader();
		readerCollection.add(reader);
	}

	public int getId() {
		return id;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
