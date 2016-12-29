package application;

public class InputDataPacket extends DataPacket
{
	@Override
	public void parseData(String line) 
	{
		if(line.length() > 1)
		{
			String markup = line.substring(line.indexOf("<"), line.indexOf(">"));
			String data = line.substring(line.indexOf(">") + 1).trim();
			
			switch(markup)
			{
				case "<COM>": setComID(data);
					break;
				case "<DEV>": setDeviceID(data);
					break;
				case "<DATA>": saveData(data);
					break;
			}
		}
	}
}