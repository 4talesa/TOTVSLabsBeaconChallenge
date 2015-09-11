package farto.cleva.guilherme.totvs.framework.beacons;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;
import com.estimote.sdk.Utils.Proximity;

public class BeaconHelper {

	public static final String DEFAULT_ESTIMOTE_BEACON_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	public static final String DEFAULT_ESTIMOTE_BEACON_MINOR = "1";

	private Context context = null;

	private BeaconManager beaconManager;

	private boolean isConnected = false;

	private List<Region> regions = new LinkedList<Region>();

	private static BeaconHelper instance = null;

	public static BeaconHelper getInstance() {
		if (instance == null) {
			instance = new BeaconHelper();
		}

		return instance;
	}

	private BeaconHelper() {
		super();
	}

	public void initialize(Context context, long scanPeriodSeconds, final Proximity proximity, final OnBeaconsDiscovered onBeaconsDiscovered) {
		this.context = context;

		if (beaconManager != null && isConnected) {
			disconnect();
		}

		if (beaconManager == null) {
			beaconManager = new BeaconManager(this.context);
			beaconManager.setBackgroundScanPeriod(TimeUnit.SECONDS.toMillis(scanPeriodSeconds), 0);

			beaconManager.setRangingListener(new BeaconManager.RangingListener() {

				@Override
				public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {

					for (Beacon beacon : beacons) {
						if (proximity == Utils.computeProximity(beacon)) {
							if (onBeaconsDiscovered != null) {
								onBeaconsDiscovered.execute(region, beacon);
							}
						}
					}
				}
			});
		}
	}

	public interface OnBeaconsDiscovered {

		public void execute(Region region, Beacon beacon);

	}

	public interface OnConnect {

		public void execute(List<Region> regions);

	}

	public void startRanging(final OnConnect onConnect) {
		if (beaconManager.hasBluetooth() && beaconManager.isBluetoothEnabled()) {
			beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
				@Override
				public void onServiceReady() {
					isConnected = true;

					try {
						clearRegions();

						if (onConnect != null) {
							onConnect.execute(regions);
						}

						if (regions != null && !regions.isEmpty()) {
							for (Region region : getRegions()) {
								beaconManager.startRanging(region);
							}
						}
					} catch (RemoteException e) {
						Log.e("E", "Cannot start ranging", e);
					}
				}
			});
		}
	}

	private void clearRegions() {
		getRegions().clear();
	}

	public List<Region> getRegions() {
		if (regions == null) {
			regions = new LinkedList<Region>();
		}

		return regions;
	}

	public void stopRanging() {
		try {
			for (Region region : getRegions()) {
				beaconManager.stopRanging(region);
			}
		} catch (RemoteException e) {
			Log.e("E", "Cannot stop ranging but it does not matter now", e);
		}
	}

	public void disconnect() {
		beaconManager.disconnect();

		isConnected = false;
	}

}
