package parohyapp.navigationtracker.handler.marker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by tomas on 3/13/2016.
 */
public class AppMarker {

    private LatLng cords;
    private Marker marker;
    private String name;

    public AppMarker(LatLng cords, String name){
        this.cords = cords;
        this.name = name;
    }

    public AppMarker(double latitude, double longitude, String name){
        this.cords = new LatLng(latitude,longitude);
        this.name = name;
    }

    public LatLng getCords() {
        return cords;
    }

    public void setCords(LatLng cords) {
        this.cords = cords;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
