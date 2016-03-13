package parohyapp.navigationtracker.handler.localization;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
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
        switch (status){
            case LocationProvider.AVAILABLE:
                listener.positionListenerStatus("GPS is available");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                listener.positionListenerStatus("GPS out of service");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                listener.positionListenerStatus("GPS is unavailable");
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {
        listener.positionListenerStatus("GPS is enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        listener.positionListenerStatus("GPS is disabled");
    }
}
