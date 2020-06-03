package gmba.runningapp.control.runlogic;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;
import gmba.runningapp.control.gps.GPSLogic;
import gmba.runningapp.control.gps.GPSLogicImpl;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;

import java.util.LinkedList;
import java.util.List;

public class RunLogicImpl implements RunLogic{
    private Context ctx;
    private long startTime;
    private GPSLogic gps;
    private Location lastlocation;
    private List<double[]> route;
    private List<Location> allData;
    private int distance = 0;

    public RunLogicImpl(Context ctx){
        this.ctx = ctx;
        route = new LinkedList<>();
        allData = new LinkedList<>();

    }

    @Override
    public void createRoutePoint(){
        allData = gps.retrieveLocationList();
        route.add(meanLocation(allData));
        Toast.makeText(ctx,"routePoint created",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void startRun() {
        gps = new GPSLogicImpl(ctx,1000);
        this.startTime = System.currentTimeMillis();
        gps.startLocationUpdates();
    }

    @Override
    public Run endRun() {
        gps.stopLocationUpdates();
        long currentTime = System.currentTimeMillis();
        long millis = (currentTime-startTime);
        int time = (int) (millis / 1000) % 60 ;
        return new RunImpl(1,time,this.distance,1,new LinkedList<Coordinates>());
    }

    @Override
    public int[] getTime() {
        long currentTime = System.currentTimeMillis();
        long millis = (currentTime-startTime);
        if(millis < 0){
            throw new IllegalArgumentException("passed should be greater than 0");
        }
        int[] time = new int[3];
        time[0] = (int) (millis / 1000) % 60 ;
        time[1] = (int) ((millis / (1000*60)) % 60);
        time[2] = (int) ((millis / (1000*60*60)) % 24);
        return time;
    }

    @Override
    public int getDistance() {
        Location currentLocation = gps.getGPSLocation();
        if(lastlocation != null){
            distance = distance + (int) currentLocation.distanceTo(lastlocation);
        }else{
           // Toast.makeText(ctx, "Last Location was null", Toast.LENGTH_SHORT).show();
        }
        lastlocation = currentLocation;
        return distance;
    }

    @Override
    public void saveRun(Run run) throws NullPointerException {
        if(null == run){
            throw new NullPointerException("run is null");
        }
    }
    private double[] meanLocation(List<Location> locList){
        int count = locList.size();
        double lat = 0;
        double lng = 0;
        double[] mean = new double[2];
        if(locList.size() <=2) {
            throw new IllegalArgumentException("not enough locations");
        }
        for(Location l: locList){
            lat = lat+l.getLatitude();
            lng = lng+l.getLongitude();
        }
        mean[0] = lat / count;
        mean[1] = lng / count;
        return mean;
    }


    //a few Helper for Testing
    public long getStartTime() {
        return startTime;
    }

    public void setDistance(int meters){
        this.distance = meters;
    }
}
