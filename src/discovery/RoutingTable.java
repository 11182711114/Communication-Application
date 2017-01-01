package discovery;

import java.util.ArrayList;
import java.util.List;

import application.Device;

public class RoutingTable {
	private List<Device> routing;
	// {$deviceID,$IP}

	public RoutingTable(List<Device> table) {
		this.routing = table;
	}

	public void addDevice(Device d) {
		if (routing == null)
			return;
		routing.add(d);
	}

	public void reset() {
		routing = new ArrayList<Device>();
	}

	public String[] toPrint() {
		synchronized (routing) {
			String[] devices = new String[routing.size()];
			if (devices.length == 0)
				return null;

			for (int i = 0; i < routing.size(); i++) {
				devices[i] = routing.get(i).toPrint();
			}
			return devices;
		}
	}

	public String[] getDevicesAsStrings() {
		String[] tmp = new String[routing.size()];
		for (int i = 0; i < tmp.length; i++) {
			tmp[i] = routing.get(i).toPrint();
		}
		return tmp;
	}

	public void clear() {
		routing.clear();
	}
}
