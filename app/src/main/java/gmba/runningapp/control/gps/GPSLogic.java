package gmba.runningapp.control.gps;

import android.location.Location;
import gmba.runningapp.exceptions.NoGPSSensorException;

import java.util.List;

public interface GPSLogic {

    /**
     * starts the GPS Locations requests of the GPS Sensor
     * @throws NoGPSSensorException if GPS Sensor is null
     */
    void startLocationUpdates() throws NoGPSSensorException;

    /**
     * the GPS sensor stops asking for Locations
     * @throws NoGPSSensorException if GPS Sensor is null
     */
    void stopLocationUpdates()throws NoGPSSensorException;

    /**
     * gets all locations from when this function was last called until now
     * @return the List of all Locations, only return the list if there are 2 or more Locations
     *         otherwise the List is empty.
     *         If the GPS logic has only 1 Location it will be saved for the next method call
     */
    List<Location> retrieveLocationList();

}
