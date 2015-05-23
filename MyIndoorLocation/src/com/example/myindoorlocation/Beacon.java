package com.example.myindoorlocation;

import android.bluetooth.BluetoothDevice;

public class Beacon {
	private BluetoothDevice device;
	private int RSSI;
	private byte[] scanRecord;

	public Beacon(BluetoothDevice d, int r, byte[] s) {
		this.device = d;
		this.RSSI = r;
		this.scanRecord = s;
	}
	
	public String getAddress(){
		return this.device.getAddress();
	}
	
	public int getRSSI(){
		return RSSI;
	}

}
