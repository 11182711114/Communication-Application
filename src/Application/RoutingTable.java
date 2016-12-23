package Application;

import java.util.ArrayList;
import java.util.List;

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
}
