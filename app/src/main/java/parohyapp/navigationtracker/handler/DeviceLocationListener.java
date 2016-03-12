package parohyapp.navigationtracker.handler;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by tomas on 3/12/2016.
 */
public class DeviceLocationListener implements LocationListener {

    PositionListener listener;

    public DeviceLocationListener(PositionListener listener){
        this.listener = listener;
    }

    @Override
    public void onLocationChanged(Location location) {
        listener.updateMyLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
