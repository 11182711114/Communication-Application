package discovery;

import java.util.List;

import application.Device;

public class RoutingTableOutputGenerator {

	public static String[] generateRoutingTableAsStringArray(RoutingTable rt) {
		List<Device> devices = rt.getRouting();
		
		String[] tmp = new String[devices.size()+2];
		tmp[0] = "START";
		for (int i = 0; i < tmp.length-2; i++) {
			tmp[i+1] = devices.get(i).toPrint();
		}
		tmp[tmp.length-1] = "END";
		return tmp;
	}
}
