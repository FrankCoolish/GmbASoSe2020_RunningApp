package gmba.runningapp.view.runningGUI.history;

import android.widget.Toast;
import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;
import gmba.runningapp.R;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.exceptions.RunNotFoundException;
import gmba.runningapp.model.datenspeicher.Data;
import gmba.runningapp.model.datenspeicher.DataSingletonImpl;
import gmba.runningapp.model.datenspeicher.TestDB;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.CoordinatesImpl;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;

import java.util.LinkedList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
                                                                GoogleMap.OnPolylineClickListener,
                                                                GoogleMap.OnPolygonClickListener {
    private List<Coordinates> mRoute;
    private UserVerwaltung userVerwaltung;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        int runId = getIntent().getIntExtra(RunListActivity.SELECTED_RUN_MESSAGE,0);
        userVerwaltung = new UserVerwaltungImpl(this);
        user = userVerwaltung.getCurrentUserName();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ;

       // mRoute = data.loadUserData("Frank").getHistory().get(1).getCoordinates();
        try {
            mRoute = userVerwaltung.loadRun(user,runId).getCoordinates();
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
        } catch (InvalidValueException e) {
            e.printStackTrace();
        } catch (RunNotFoundException e) {
            e.printStackTrace();
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add polylines and polygons to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(true));


        List<LatLng> points = new LinkedList<>();
        for(Coordinates c: mRoute){
            points.add(new LatLng(c.getCoordinates()[0],c.getCoordinates()[1]));
        }
        polyline1.setPoints(points);

        if(mRoute.size()>0) {
            LatLng start = new LatLng(mRoute.get(0).getCoordinates()[0], mRoute.get(0).getCoordinates()[1]);


            // Position the map's camera near Alice Springs in the center of Australia,
            // and set the zoom factor so most of Australia shows on the screen.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 16));

            // Set listeners for click events.
            googleMap.setOnPolylineClickListener(this);
            googleMap.setOnPolygonClickListener(this);
        }

    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}
