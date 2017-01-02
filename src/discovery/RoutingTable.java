package discovery;

import java.util.ArrayList;
import java.util.List;

import interDeviceCommunication.Device;

public class RoutingTable {
	private List<Device> routing;
	// {$deviceID,$IP}

	public RoutingTable(List<Device> table) {
		this.routing = table;
	}

	public void addDevice(Device d) {
		routing.add(d);
	}
	
	public void updateDevices(Device[] devices){
		routing.clear();
		for(Device d : devices){
			routing.add(d);
		}
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

	public List<Device> getRouting() {
		return routing;
	}

	public void clear() {
		routing.clear();
	}
}
