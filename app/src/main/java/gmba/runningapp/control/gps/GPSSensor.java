package gmba.runningapp.control.gps;

import android.content.Context;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.*;


public class GPSSensor {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest mlocationRequest;
    private LocationCallback locationCallback;
    private final int REQUEST_INTERVALL; //10000 10s
    private Context ctx;
    private GPSLogicImpl gpsLogic;

    public GPSSensor(Context ctx, int intervall, GPSLogicImpl gpsLogic) {
        this.ctx = ctx;
        REQUEST_INTERVALL = intervall;
        this.gpsLogic = gpsLogic;
        init();
    }

    private void init(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ctx);
        locationCallback = createLocationCallback();
        mlocationRequest = createLocationRequest();
    }

    public void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(mlocationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationCallback createLocationCallback(){
        LocationCallback mlocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    sendLocation(location);
                }
            }
        };
        return mlocationCallback;
    }

    private LocationRequest createLocationRequest(){
        LocationRequest newlocationRequest = LocationRequest.create();
        newlocationRequest.setInterval(REQUEST_INTERVALL);
        newlocationRequest.setFastestInterval(2000);
        newlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return newlocationRequest;
    }

    private void sendLocation(Location location){
        gpsLogic.updateLastLocation(location);
    }

}
