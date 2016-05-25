package parohyapp.navigationtracker.handler.marker;

import android.content.res.AssetManager;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tomas on 3/13/2016.
 */
public class MarkerHandler {

    private GoogleMap googleMap;
    private AssetManager assetManager;
    private ArrayList<AppMarker> markers;

    public MarkerHandler(GoogleMap gMap, AssetManager assetManager){
        googleMap = gMap;
        markers = new ArrayList<>();
        this.assetManager = assetManager;
    }

    public boolean removeMarker(int id){
        int i = 0;
        for(AppMarker tmp : markers){
            if(tmp instanceof MarkerBLE){
                MarkerBLE tmpBleMarker = (MarkerBLE) tmp;
                if(tmpBleMarker.getId() == id){
                    tmpBleMarker.getMarker().remove(); //remove marker from map
                    markers.remove(i); //remove AppMarker from array
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    public boolean removeMarker(MarkerBLE marker){
        int i = 0;
        for(AppMarker tmp : markers){
            if(tmp.equals(marker)){
                tmp.getMarker().remove(); //remove marker from map
                markers.remove(i); //remove AppMarker from array
                return true;
            }
            i++;
        }
        return false;
    }


    public Marker addMarkerBLE(MarkerBLE marker){
        if(googleMap != null){
            Marker mapMarker = googleMap.addMarker(new MarkerOptions().position(marker.getCords()).title(marker.getName())); //insert marker in map and return its instance
            marker.setMarker(mapMarker); //mapMarker reference added to AppMarker
            markers.add(marker); //AppMarker stored in array
            return mapMarker;
        }
        return null;
    }

    public Marker addMarkerBLE(LatLng position, String title){
        if(googleMap != null){
            Marker mapMarker = googleMap.addMarker(new MarkerOptions().position(position).title(title)); //insert marker in map and return its instance
            AppMarker marker = new AppMarker(position,title); //create an instace of AppMarker
            marker.setMarker(mapMarker); //mapMarker reference added to AppMarker
            markers.add(marker); //AppMarker stored in array
            return mapMarker;
        }
        return null;
    }


    public void loadBeaconMarkers(){
        try {
            JSONObject config = new JSONObject(loadJSONFromAsset()).getJSONObject("C1:48:9A:1E:00:E5").getJSONObject("config");
            long longitude = config.getLong("longitude");
            long latitude = config.getLong("latitude");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = assetManager.open("beacons.json");
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
}
