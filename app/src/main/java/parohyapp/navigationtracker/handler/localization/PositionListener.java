package parohyapp.navigationtracker.handler.localization;

/**
 * Created by tomas on 3/12/2016.
 */
public interface PositionListener {
    public void updateMyLocation(double latitude, double longitude);
    public void positionListenerStatus(String msg);
}
