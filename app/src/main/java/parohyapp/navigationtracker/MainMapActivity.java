package parohyapp.navigationtracker;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import parohyapp.navigationtracker.Locations.LocationListActivity;
import parohyapp.navigationtracker.fragments.Settings;
import parohyapp.navigationtracker.handler.localization.DeviceLocationListener;
import parohyapp.navigationtracker.handler.localization.PositionListener;
import parohyapp.navigationtracker.handler.marker.MarkerBLE;
import parohyapp.navigationtracker.handler.marker.MarkerHandler;

public class MainMapActivity extends FragmentActivity implements OnMapReadyCallback, PositionListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng myCords;

    private MarkerHandler markerHandler;
    private Marker myMarker;
    private Settings settingsFragment;

    private BeaconChecker bc;
    private Set<String> nearbyBeacons;
    private Map<String, Integer> beaconDestroy;

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

        bc = new BeaconChecker();
        nearbyBeacons = new HashSet<>();
        beaconDestroy = new HashMap<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bc.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        bc.onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerHandler = new MarkerHandler(googleMap);

        Location myLocation = getDeviceLocation();
        if (myLocation != null) {
            updateMyLocation(myLocation.getLatitude(), myLocation.getLongitude());
        }

        if (myCords != null) {
            if (myMarker != null) {
                myMarker.remove();
            }
            //myMarker = mMap.addMarker(new MarkerOptions().position(myCords).title("You"));

            myMarker = markerHandler.addMarker(myCords, "You");
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));

            //testing dummy
            MarkerBLE marker1 = new MarkerBLE(new LatLng(myCords.latitude + 1, myCords.longitude + 1), 1, "Marker1");
            markerHandler.addMarkerBLE(marker1);
        }
    }

    private Location getDeviceLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener mlocListener = new DeviceLocationListener(this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
        return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    }

    @Override
    public void updateMyLocation(double latitude, double longitude) {
        myCords = new LatLng(latitude, longitude);

        //TODO needs testing
        if (myMarker != null) {
            myMarker.remove();
        }
        myMarker = markerHandler.addMarker(myCords, "You");
    }

    @Override
    public void positionListenerStatus(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void resetCamera(View v) {
        if (myCords != null) {
            mMap.moveCamera(CameraUpdateFactory.zoomTo(13.5f));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCords));
        }
    }

    public void openSettings(View v) {
        //TODO style settings
        //TODO implement settings listener
        settingsFragment.toggle();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("MarkerClick", marker.getTitle());
        return false;
    }

    /*DEBUG BUTTON*/
    public void myClick(View v) {
        Intent intent = new Intent(MainMapActivity.this, LocationListActivity.class);
        JSONObject beacons;
        JSONObject locations;
        try {
            beacons = new JSONObject(loadJSONFromAsset());
            locations = beacons.getJSONObject("C1:48:9A:1E:00:E5");
        } catch (JSONException e) {
            e.printStackTrace();
            locations = new JSONObject();
        }
        intent.putExtra("beacons", locations.toString());
        startActivity(intent);
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getAssets().open("beacons.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * Akademia SOVY project
     * <p/>
     * Copyright - 2016
     * Created by Peter Varholak on 30. 4. 2016.
     */
    public class BeaconChecker {

        public static final int SCAN_PERIOD = 15000;

        private BluetoothAdapter bluetoothAdapter;
        private Handler handler;
        private BluetoothLeScanner leScanner;
        private ScanSettings settings;
        private List<ScanFilter> filters;


        public BeaconChecker() {
            handler = new Handler();

            BluetoothManager btManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            bluetoothAdapter = btManager.getAdapter();
        }

        public void onResume() {
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, 1);
            } else {
                if (Build.VERSION.SDK_INT >= 21) {
                    leScanner = bluetoothAdapter.getBluetoothLeScanner();
                    settings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_BALANCED).build();
                    filters = new ArrayList<>();
                }
                scanLeDevices(true);
            }
        }

        public void onPause() {
            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                scanLeDevices(false);
            }
        }

        private void scanLeDevices(boolean enable) {
            if (enable) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT < 21) {
                            Log.e("leScanCallback", "leScanCallback");
                            //bluetoothAdapter.stopLeScan(mLeScanCallback);
                        } else {
                            leScanner.startScan(mScanCallback);
                            //leScanner.startScan(filters, settings, mScanCallback);
                        }
                    }
                }, SCAN_PERIOD);
                /*if (Build.VERSION.SDK_INT < 21) {
                    Log.e("leScanCallback", "leScanCallback");
                    //bluetoothAdapter.startLeScan(mLeScanCallback);
                } else {
                    leScanner.startScan(filters, settings, mScanCallback);
                }*/
            } else {
                if (Build.VERSION.SDK_INT < 21) {
                    Log.e("leScanCallback", "leScanCallback");
                    //bluetoothAdapter.stopLeScan(mLeScanCallback);
                } else {
                    leScanner.stopScan(mScanCallback);
                }
            }
        }

        private ScanCallback mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                String beaconID = String.valueOf(result.getDevice());
                Log.d("device ID", beaconID);
                if (beaconID != null) {
                    if (nearbyBeacons.add(beaconID)) {
                        Toast.makeText(MainMapActivity.this, "You are near beacon id: " + beaconID, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainMapActivity.this, LocationListActivity.class);
                        JSONObject beacons;
                        JSONObject locations;
                        try {
                            beacons = new JSONObject(loadJSONFromAsset());
                            locations = beacons.getJSONObject(beaconID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            locations = new JSONObject();
                        }
                        intent.putExtra("beacons", locations.toString());
                        startActivity(intent);
                    }
                    beaconDestroy.put(beaconID, 15);
                }

                checkBeacons();
                scanLeDevices(true);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                for (ScanResult sr : results) {
                    Log.i("ScanResult-Results", sr.toString());
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                //Log.e("Scan failed", errorCode + "");
            }
        };

        private void checkBeacons() {
            for (String beaconID : nearbyBeacons) {
                int counter = beaconDestroy.get(beaconID);
                if (counter == 0) {
                    beaconDestroy.remove(beaconID);
                    nearbyBeacons.remove(beaconID);
                    Toast.makeText(MainMapActivity.this, "Beacon destroyed", Toast.LENGTH_LONG).show();
                } else {
                    beaconDestroy.put(beaconID, counter - 1);
                }
            }
        }
    }
}
