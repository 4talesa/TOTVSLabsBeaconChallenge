package farto.cleva.guilherme.totvs.framework.listeners;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GNSSListener implements LocationListener {

	private static final NumberFormat fmt = new DecimalFormat("#.000000");

	private OnLocationChanged onConnect = null;

	public interface OnLocationChanged {

		public void execute(String latitude, String longitude);

	}

	public GNSSListener(OnLocationChanged onConnect) {
		super();

		this.onConnect = onConnect;
	}

	@Override
	public void onLocationChanged(Location location) {
		String latitude = fmt.format(location.getLatitude());
		String longitude = fmt.format(location.getLongitude());

		if (onConnect != null) {
			onConnect.execute(latitude, longitude);
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

}
