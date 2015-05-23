package com.example.myindoorlocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	private DeviceAdapter mDeviceAdapter = DeviceAdapter.getInstance();
	private int recordCount = -1;
	
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, final int RSSI,
				final byte[] scanRecord) {
			final Beacon beacon = new Beacon(device, RSSI, scanRecord);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mDeviceAdapter.addDevice(beacon);
					HashMap<String, Integer> deviceMap = mDeviceAdapter
							.getDeviceHashMap();
					Iterator it = deviceMap.entrySet().iterator();
					String str = new String();
					while (it.hasNext()) {
						Map.Entry entry = (Entry) it.next();
						str += "\nAddress:\t" + entry.getKey() + "\tRSSI:\t"
								+ entry.getValue();
					}
					TextView addressRSSI = (TextView) findViewById(R.id.AddressRSSI);
					addressRSSI.setText(str);
				}
			});
		}
	};

	public void recordPos(View v) {
		TextView t = (TextView) findViewById(R.id.historyTXT);
		ScrollView sc = (ScrollView) findViewById(R.id.scrollView);
		sc.fullScroll(ScrollView.FOCUS_DOWN);

		mDeviceAdapter.addRecord();

		String historyTXT = (String) t.getText();
		recordCount += 1;
		HashMap<String, Integer> deviceMap = mDeviceAdapter.getDeviceHashMap();
		Iterator it = deviceMap.entrySet().iterator();
		String currentPairs = new String();
		while (it.hasNext()) {
			Map.Entry entry = (Entry) it.next();
			currentPairs += "  " + entry.getKey().toString().substring(0, 2)
					+ entry.getValue();
		}
		historyTXT += "\n" + recordCount + ":" + currentPairs;
		t.setText(historyTXT);
	}

	public void matchPos(View v) {
		TextView t = (TextView) findViewById(R.id.resultTXT);
		ScrollView sc = (ScrollView) findViewById(R.id.resultScrollView);
		sc.fling(Integer.MAX_VALUE);

		t.setText(mDeviceAdapter.getDifferenceInfo() + "\n当前位置序号："
				+ mDeviceAdapter.getPos());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView logTXT = (TextView) findViewById(R.id.logTXT);
		logTXT.setText("App is Launching!");

		if (getPackageManager().hasSystemFeature(
				getPackageManager().FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, "支持蓝牙", Toast.LENGTH_SHORT).show();
		}

		BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter != null) {
			Toast.makeText(this, "蓝牙适配器建立成功", Toast.LENGTH_SHORT).show();
		}
		mBluetoothAdapter.enable();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		mBluetoothAdapter.startLeScan(mLeScanCallback);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
