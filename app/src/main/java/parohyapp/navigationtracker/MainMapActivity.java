package parohyapp.navigationtracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import parohyapp.navigationtracker.fragments.Settings;
import parohyapp.navigationtracker.handler.localization.DeviceLocationListener;
import parohyapp.navigationtracker.handler.localization.PositionListener;
import parohyapp.navigationtracker.handler.marker.MarkerBLE;
import parohyapp.navigationtracker.handler.marker.MarkerHandler;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback,PositionListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng myCords;

    private MarkerHandler markerHandler;
    private Marker myMarker;
    private Settings settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        settingsFragment = (Settings) getSupportFragmentManager().findFragmentById(R.id.settings_fragment);
        settingsFragment.toggle();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerHandler = new MarkerHandler(googleMap);

        Location myLocation = getDeviceLocation();
        updateMyLocation(myLocation.getLatitude(), myLocation.getLongitude());

        if(myCords != null){
            if(myMarker!=null){
                myMarker.remove();
            }
            //myMarker = mMap.addMarker(new MarkerOptions().position(myCords).title("You"));

            myMarker = markerHandler.addMarker(myCords,"You");
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));
        }

        //testing dummy
        MarkerBLE marker1 = new MarkerBLE(new LatLng(myCords.latitude+1,myCords.longitude+1),1,"Marker1");
        markerHandler.addMarkerBLE(marker1);
    }

    private Location getDeviceLocation(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new DeviceLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void updateMyLocation(double latitude, double longitude) {
        myCords = new LatLng(latitude,longitude);

        //TODO needs testing
        if(myMarker!=null){
            myMarker.remove();
        }
        myMarker = markerHandler.addMarker(myCords,"You");
    }

    @Override
    public void positionListenerStatus(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void resetCamera(View v){
        if(myCords != null){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));
        }
    }

    public void openSettings(View v){
        //TODO style settings
        //TODO implement settings listener
        settingsFragment.toggle();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MarkerClick", marker.getTitle());
        return false;
    }
}
