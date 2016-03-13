package parohyapp.navigationtracker.handler.marker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by tomas on 3/13/2016.
 */
public class MarkerHandler {

    private GoogleMap googleMap;
    private ArrayList<AppMarker> markers;

    public MarkerHandler(GoogleMap gMap){
        googleMap = gMap;
        markers = new ArrayList<>();
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

    public Marker addMarker(LatLng position, String title){
        if(googleMap != null){
            Marker mapMarker = googleMap.addMarker(new MarkerOptions().position(position).title(title)); //insert marker in map and return its instance
            AppMarker marker = new AppMarker(position,title); //create an instace of AppMarker
            marker.setMarker(mapMarker); //mapMarker reference added to AppMarker
            markers.add(marker); //AppMarker stored in array
            return mapMarker;
        }
        return null;
    }

}
