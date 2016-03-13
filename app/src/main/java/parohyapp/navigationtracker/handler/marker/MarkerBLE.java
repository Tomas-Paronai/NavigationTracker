package parohyapp.navigationtracker.handler.marker;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by tomas on 3/13/2016.
 */
public class MarkerBLE extends AppMarker{

    private int id;

    public MarkerBLE(LatLng cords, int id, String name){
        super(cords,name);
        this.id = id;
    }

    public MarkerBLE(double latitude, double longitude, int id, String name){
        super(latitude,longitude,name);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
