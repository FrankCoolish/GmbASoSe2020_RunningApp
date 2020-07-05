package gmba.runningapp.view.runningGUI.user;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import gmba.runningapp.R;
import gmba.runningapp.model.datenspeicher.DataSingletonImpl;
import gmba.runningapp.view.runningGUI.history.RunListActivity;
import gmba.runningapp.view.runningGUI.profil.ProfileActivity;
import gmba.runningapp.view.runningGUI.run.RunActivity;


public class UserActivity extends AppCompatActivity {
    private String currentUser = "default";
    public static final String CURRENT_USER_MESSAGE = "runningApp.currentUser";
    public static final String RUNDETECTION = "runningApp.rundetection";
    private boolean rundetectionOn = false;
    private boolean locationPermission = false;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        TextView user = findViewById(R.id.profileUserNameTextView);
        currentUser = DataSingletonImpl.getDataInstance(this).getCurrentUser();
        user.setText(currentUser);
        //locationPermission =  checkLocationPermission();


        Switch witch = findViewById(R.id.runDetectionSwitch);
        witch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    rundetectionOn = true;
                } else {
                    // The toggle is disabled
                    rundetectionOn = false;
                }
            }
        });
    }

    public void onClickProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra(CURRENT_USER_MESSAGE,currentUser);
        intent.putExtra(RUNDETECTION,rundetectionOn);
        startActivity(intent);
    }

    public void onClickStartRun(View view){
        locationPermission = checkLocationPermission();
        if(locationPermission){
            Intent intent = new Intent(this, RunActivity.class);
            intent.putExtra(CURRENT_USER_MESSAGE,currentUser);
            intent.putExtra(RUNDETECTION,rundetectionOn);
            startActivity(intent);
        }else{
            Toast.makeText(this, "No location permission!", Toast.LENGTH_SHORT).show();
        }


    }




    public void onClickStartHistory(View view){
        Intent intent = new Intent(this, RunListActivity.class);
        startActivity(intent);
    }



    ///////////////////////////////////////////////////////////////////
    //////////////       Permissioncheck                    ///////////
    ///////////////////////////////////////////////////////////////////

    //https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(UserActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}
