package gmba.runningapp.control.gps;

import android.location.Location;
import gmba.runningapp.exceptions.NoGPSSensorException;

import java.util.LinkedList;
import java.util.List;


public class GPSLogicImpl implements GPSLogic{
    private GPSSensor sensor;
    private List<Location> locationList;


    //getter and setter for Testpurposes
    public void setSensor(GPSSensor sensor) {
        this.sensor = sensor;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public GPSLogicImpl() {
        locationList = new LinkedList<>();
    }

    //used by the GPS Sensor
    protected void updateLastLocation (Location location){
        locationList.add(location);
    }

    @Override
    public void startLocationUpdates() throws NoGPSSensorException {
        if(null != sensor){
            sensor.startLocationUpdates();
        }else{
            throw new NoGPSSensorException("GPS Sensor not set");
        }
    }

    @Override
    public void stopLocationUpdates() throws NoGPSSensorException {
        if(null != sensor) {
            sensor.stopLocationUpdates();
        }else{
        throw new NoGPSSensorException("GPS Sensor not set");
        }
    }

    @Override
    public List<Location> retrieveLocationList(){
        List<Location> l = new LinkedList<>();
        if(locationList.size()>1){
            l.addAll(locationList);
            locationList.clear();
        }
        return l;
    }

}
