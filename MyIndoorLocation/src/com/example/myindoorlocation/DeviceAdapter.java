package com.example.myindoorlocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.util.Log;
import android.widget.TextView;

public class DeviceAdapter {
	private static DeviceAdapter instance = null;
	private HashMap<String, Integer> Devices = new HashMap<String, Integer>();
	private ArrayList<HashMap<String, Integer>> recordList = new ArrayList<HashMap<String, Integer>>();

	private DeviceAdapter() {
	}

	public static DeviceAdapter getInstance() {
		if (instance == null) {
			instance = new DeviceAdapter();
		}
		return instance;
	}

	public void addDevice(Beacon beacon) {
		int temp = 0;
		if (this.Devices.containsKey(beacon.getAddress())) {
			temp = Devices.get(beacon.getAddress());
			Devices.remove(beacon.getAddress());
		}
		double result = 0.5 * temp + 0.5 * beacon.getRSSI();
		Devices.put(beacon.getAddress(), (int) result);
	}

	public HashMap<String, Integer> getDeviceHashMap() {
		return this.Devices;
	}

	public void addRecord() {
		HashMap<String, Integer> temp = new HashMap<>();
		Iterator it = Devices.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			Integer t = (Integer) entry.getValue() + 0;
			temp.put("" + entry.getKey(), t);
		}
		recordList.add(temp);
	}

	public ArrayList<HashMap<String, Integer>> getRecordList() {
		return this.recordList;
	}

	public int calculateDifference(HashMap<String, Integer> m,
			HashMap<String, Integer> n) {
		Iterator mIt = m.entrySet().iterator();
		Iterator nIt = n.entrySet().iterator();

		int difference = 0;
		if (m.size() > n.size()) {
			while (mIt.hasNext()) {
				Map.Entry mEntry = (Entry) mIt.next();
				String mItAddr = mEntry.getKey().toString();
				if (n.containsKey(mItAddr)) {
					difference += Math.abs(n.get(mItAddr).doubleValue()
							- m.get(mItAddr).doubleValue());
				} else {
					difference += Math.abs(m.get(mItAddr));
				}
			}
		} else {
			while (nIt.hasNext()) {
				Map.Entry nEntry = (Entry) nIt.next();
				String nItAddr = nEntry.getKey().toString();
				if (m.containsKey(nItAddr)) {
					difference += Math.abs(n.get(nItAddr).doubleValue()
							- m.get(nItAddr).doubleValue());
				} else {
					difference += Math.abs(n.get(nItAddr));
				}

			}
		}
		return difference;
	}

	public String getMapInfo(HashMap<String, Integer> s) {
		String result = "";
		Iterator it = s.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			result += entry.getKey().toString().substring(0, 2) + "|"
					+ entry.getValue() + "|";
		}
		return result;
	}

	public int getPos() {
		int max = Integer.MAX_VALUE;
		int posIndex = 0;
		for (int i = 0; i < recordList.size(); i++) {
			int difference = calculateDifference(Devices, recordList.get(i));
			if (max > difference) {
				max = difference;
				posIndex = i;
			}

		}
		return posIndex;
	}

	public String getDifferenceInfo() {
		String r = "";
		for (int i = 0; i < recordList.size(); i++) {
			int difference = calculateDifference(Devices, recordList.get(i));
			r += "|record" + i + ":" + difference + "|" + "\n";
		}
		return r;
	}
	// private HashMap<String, ArrayList<Integer>> addressRSSImap = new
	// HashMap<>();
	// private int accuracy = 5;
	//
	// public HashMap<String, ArrayList<Integer>> getAddressRSSImap() {
	// return addressRSSImap;
	// }
	//
	// public void updateDevice(Beacon beacon) {
	// if (getAddressRSSImap().containsKey(beacon.getAddress())) {
	//
	// } else {
	// ArrayList<Integer> rssiQueue = new ArrayList<>(this.accuracy);
	// rssiQueue.add(beacon.getRSSI());
	// this.addressRSSImap.put(beacon.getAddress(), rssiQueue);
	// }
	// }
}
