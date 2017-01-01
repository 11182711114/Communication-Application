package discovery;

import java.util.List;

import application.Device;

public class RoutingTableOutputGenerator {

	public static String[] generateRoutingTableAsStringArray(RoutingTable rt) {
		List<Device> devices = rt.getRouting();
		
		String[] tmp = new String[devices.size()];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = devices.get(i).toPrint();
		}
		return tmp;
	}
}
