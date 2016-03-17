package parohyapp.navigationtracker;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import parohyapp.navigationtracker.fragments.Settings;
import parohyapp.navigationtracker.handler.localization.PositionListener;
import parohyapp.navigationtracker.handler.marker.MarkerHandler;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback,
        PositionListener,
        GoogleMap.OnMarkerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private final static String TAG = "MainActivity";

    private GoogleMap mMap;
    private GoogleApiClient gApiClient;
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

//        settingsFragment = (Settings) getSupportFragmentManager().findFragmentById(R.id.settings_fragment);
//        settingsFragment.toggle();

        gApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        markerHandler = new MarkerHandler(googleMap);

    }

    @Override
    public void updateMyLocation(double latitude, double longitude) {
        Toast.makeText(this,"Lat: "+latitude+" Lon: "+longitude,Toast.LENGTH_SHORT).show();
        Log.d(TAG,"Lat: "+latitude+" Lon: "+longitude);
    }

    @Override
    public void positionListenerStatus(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    public void resetCamera(View v){

    }

    public void openSettings(View v){
        //TODO style settings
        //TODO implement settings listener

    }

    @Override
    protected void onStart() {
        gApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        gApiClient.disconnect();
        super.onStop();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MarkerClick", marker.getTitle());
        return false;
    }

    /*DEBUG BUTTON*/
    public void myClick(View v){

    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(7000);
        LocationServices.FusedLocationApi.requestLocationUpdates(gApiClient,locationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST){
            positionListenerStatus("Network connection lost");
        }
        else if(i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED){
            positionListenerStatus("Connection service disconnected");
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        updateMyLocation(location.getLatitude(),location.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
