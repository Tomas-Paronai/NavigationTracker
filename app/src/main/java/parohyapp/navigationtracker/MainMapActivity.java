package parohyapp.navigationtracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import parohyapp.navigationtracker.handler.DeviceLocationListener;
import parohyapp.navigationtracker.handler.PositionListener;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback,PositionListener {

    private GoogleMap mMap;
    private LatLng myCords;

    private Marker myMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        updateMyLocation(myLocation.getLatitude(), myLocation.getLongitude());

        LocationListener mlocListener = new DeviceLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        if(myCords != null){
            if(myMarker!=null){
                myMarker.remove();
            }
            myMarker = mMap.addMarker(new MarkerOptions().position(myCords).title("You"));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));
        }
    }

    @Override
    public void updateMyLocation(double latitude, double longitude) {
        myCords = new LatLng(latitude,longitude);

    }

    public void resetCamera(View v){
        if(myCords != null){
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));
        }
    }

    public void openSettings(View v){
        //TODO open settings activity/fragment
    }
}
