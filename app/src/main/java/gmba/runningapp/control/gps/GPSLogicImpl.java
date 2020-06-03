package gmba.runningapp.control.gps;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.*;

import java.util.LinkedList;
import java.util.List;


public class GPSLogicImpl implements GPSLogic{
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest mlocationRequest;
    private LocationCallback locationCallback;
    private Location lastLocation;
    private int REQUEUEST_INTERVALL; //10000 10s
    private List<Location> locationList;
    private Context ctx;

    public GPSLogicImpl(Context ctx, int intervall){
        init(ctx);
        locationList =new LinkedList<>();
        REQUEUEST_INTERVALL = intervall;
    }

    private void init(Context ctx){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
        this.ctx = ctx;

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    /*double[] loc = new double[2];
                    loc[0] = location.getLatitude();
                    loc[1] = location.getLongitude();
                    locationList.add(loc);*/
                    updateLastLocation(location);
                }
            }
        };
        createLocationRequest();
    }

    private void updateLastLocation(Location location){
        lastLocation = location;
        locationList.add(location);
       //Toast.makeText(ctx,"got location update", Toast.LENGTH_SHORT).show();
    }


    protected  void createLocationRequest(){
        mlocationRequest = LocationRequest.create();
        mlocationRequest.setInterval(REQUEUEST_INTERVALL);
        mlocationRequest.setFastestInterval(5000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public Location getGPSLocation() {
        return lastLocation;
    }

    @Override
    public void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(mlocationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    @Override
    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public List<Location> retrieveLocationList(){
        List<Location> l = new LinkedList<>();
        l.addAll(locationList);
        locationList.clear();
        return l;
    }
}
