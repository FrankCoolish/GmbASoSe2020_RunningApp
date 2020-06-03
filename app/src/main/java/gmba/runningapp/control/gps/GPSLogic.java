package gmba.runningapp.control.gps;

import android.location.Location;

import java.util.List;

public interface GPSLogic {
    /**
     * gives access to the last valid location the GPS sensor saved
     * @return the last Location
     *          returns null if there was not yet a Location recieved
     */
    Location getGPSLocation();

    /**
     * starts the GPS Locations requests
     */
    void startLocationUpdates();

    /**
     * the sensor stops asking for Locations
     */
    void stopLocationUpdates();

    /**
     * gets all locations from when this function was last called until now
     * @return the List of all Locations
     */
    List<Location> retrieveLocationList();

}
