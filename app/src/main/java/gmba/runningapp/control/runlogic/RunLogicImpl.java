package gmba.runningapp.control.runlogic;

import android.content.Context;
import android.location.Location;
import gmba.runningapp.control.gps.GPSLogicImpl;
import gmba.runningapp.control.gps.GPSSensor;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.exceptions.*;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.CoordinatesImpl;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class RunLogicImpl implements RunLogic{

    private UserVerwaltung userVerwaltung;
    private long startTime;
    private GPSLogicImpl gps;
    private List<Coordinates> route;
    private List<Location> allData;
    private Location lastRouteLocation = new Location("");
    private Location currentRouteLocation = new Location("");
    private double currentRoutePointsDistance = 0;
    private Run runDone;
    private String currentUser;


    public RunLogicImpl( Context ctx) throws NoGPSSensorException{
        gps = new GPSLogicImpl();
        GPSSensor sensor = new GPSSensor(ctx,2000, gps);
        gps.setSensor(sensor);
        gps.startLocationUpdates();
        userVerwaltung = new UserVerwaltungImpl(ctx);
        currentUser = userVerwaltung.getCurrentUserName();
        route = new LinkedList<>();
        allData = new LinkedList<>();
        runDone = new RunImpl(0,0,0,0,"noDate",new LinkedList<Coordinates>());
    }

    public List<Location> getAllData() {
        return allData;
    }
    public void setAllData(List<Location> list){
        this.allData = list;
    }

    public List<Coordinates> getRoute(){
        return this.route;
    }
    public void setGps(GPSLogicImpl gps){
        this.gps = gps;
    }

    public void setStartTime(long time){
        this.startTime = time;
    }

    public GPSLogicImpl getGps() {
        return gps;
    }

    @Override
    public void createRoutePoint() throws NotEnoughLocationsException{
        allData = gps.retrieveLocationList();
        Coordinates coords = meanLocation(allData);
        route.add(coords);
        lastRouteLocation.setLongitude(currentRouteLocation.getLongitude());
        lastRouteLocation.setLatitude(currentRouteLocation.getLatitude());
        currentRouteLocation.setLongitude(coords.getCoordinates()[0]);
        currentRouteLocation.setLatitude(coords.getCoordinates()[1]);
        if(route.size()>1) {
            //currentRoutePointsDistance = currentRoutePointsDistance + lastRouteLocation.distanceTo(currentRouteLocation);
            currentRoutePointsDistance = currentRoutePointsDistance + distFrom(lastRouteLocation.getLongitude(),lastRouteLocation.getLatitude(),
                                                                                         currentRouteLocation.getLongitude(),currentRouteLocation.getLatitude());
        }
    }


    @Override
    public void startRun() {
        this.currentRoutePointsDistance = 0;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public Run endRun()   {
        if(gps != null) {
            try {
                gps.stopLocationUpdates();
            } catch (NoGPSSensorException e) {
                e.printStackTrace();
            }
        }
        long currentTime = System.currentTimeMillis();
        long millis = (currentTime-startTime);
        int time = (int) (millis / 1000) % 60 ;
        int id = 0;
        try {
            id = userVerwaltung.getHighestRunId(currentUser)+1;
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
        }
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("dd-MM-yyyy").format(cDate);
        this.runDone = new RunImpl(id,time,(float)currentRoutePointsDistance,1,fDate,route);
        return runDone;
    }

    @Override
    public int[] getTime() throws InvalidValueException {
        long currentTime = System.currentTimeMillis();
        long millis = (currentTime-startTime);
        if(millis < 0){
            throw new InvalidValueException("time passed should be greater or equal 0");
        }
        int[] time = new int[3];
        time[0] = (int) (millis / 1000) % 60 ;
        time[1] = (int) ((millis / (1000*60)) % 60);
        time[2] = (int) ((millis / (1000*60*60)) % 24);
        return time;
    }

    @Override
    public double getDistance() {
        return this.currentRoutePointsDistance;
    }

    @Override
    public void saveRun() throws ParameterIsNullException, InvalidValueException, AttributeOfParamIsNullException {
        if(null == runDone){
            throw new ParameterIsNullException("run is null");
        }
            userVerwaltung.addRun(runDone,currentUser);

    }
    private Coordinates meanLocation(List<Location> locList) throws NotEnoughLocationsException {
        int count = locList.size();
        double lat = 0;
        double lng = 0;
        double[] mean = new double[2];
        if(count <2) {
            throw new NotEnoughLocationsException("not enough locations");
        }
        for(Location l: locList){
            lat = lat+l.getLatitude();
            lng = lng+l.getLongitude();
        }
        mean[0] = lat / count;
        mean[1] = lng / count;

        Coordinates coords = new CoordinatesImpl(mean[0], mean[1]);
        return coords;
    }


    //https://stackoverflow.com/questions/120283/how-can-i-measure-distance-and-create-a-bounding-box-based-on-two-latitudelongi
    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; // miles (or 6371.0 kilometers)
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;

        return dist;
    }

}
