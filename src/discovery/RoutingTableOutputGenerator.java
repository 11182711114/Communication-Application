package discovery;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import interDeviceCommunication.Device;

public class RoutingTableOutputGenerator {

	public static String[] generateRoutingTableAsStringArray(RoutingTable rt) {
		List<Device> devices = rt.getRouting();
		
		String[] tmp = new String[devices.size()+2];
		
		DateFormat df = new SimpleDateFormat("y-M-d HH:mm:ss");
		String timeOutput = df.format(System.currentTimeMillis());
		tmp[0] = "START \n" + "Valid as of: " + timeOutput;
		for (int i = 0; i < tmp.length-2; i++) {
			tmp[i+1] = devices.get(i).toPrint();
		}
		tmp[tmp.length-1] = "END";
		return tmp;
	}
}
