package gmba.runningapp.view.runningGUI.user;

import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.model.datenspeicher.DataSingletonImpl;
import gmba.runningapp.view.runningGUI.history.HistoryActivity;
import gmba.runningapp.view.runningGUI.profil.ProfileActivity;
import gmba.runningapp.view.runningGUI.run.RunActivity;


public class UserActivity extends AppCompatActivity {
    private String currentUser = "default";
    public static final String CURRENT_USER_MESSAGE = "runningApp.currentUser";
    public static final String RUNDETECTION = "runningApp.rundetection";
    private boolean rundetectionOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        TextView user = findViewById(R.id.profileUserNameTextView);
        currentUser = DataSingletonImpl.getDataInstance(this).getCurrentUser();
        user.setText(currentUser);


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
        Intent intent = new Intent(this, RunActivity.class);
        intent.putExtra(CURRENT_USER_MESSAGE,currentUser);
        startActivity(intent);

    }




    public void onClickStartHistory(View view){
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}
