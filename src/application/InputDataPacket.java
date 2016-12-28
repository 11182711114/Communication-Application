
package application;

public class InputDataPacket extends DataPacket
{
	@Override
	public void parseData(String line) 
	{
		String markup = line.substring(line.indexOf("<"), line.indexOf(">") + 1);
		String data = line.substring(line.indexOf(">") + 1).trim();
		
		switch(markup)
		{
			case "<COM>": setComID(data);
				break;
			case "<DEV>": setDeviceID(data);
				break;
			case "<DATA>": saveData(data);
				break;
			case "<CHECK>": checkSum(data);
		}
		
	}
}