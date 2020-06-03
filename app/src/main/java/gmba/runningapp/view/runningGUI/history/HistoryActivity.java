package gmba.runningapp.view.runningGUI.history;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.control.userverwaltung.UserVerwaltung;
import gmba.runningapp.control.userverwaltung.UserVerwaltungImpl;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;

import java.util.LinkedList;

public class HistoryActivity extends AppCompatActivity {
    private UserVerwaltung userVerwaltung;
    private Run testRun;
    private Run runToSave;
    private TextView tvId;
    private TextView tvTime;
    private TextView tvDistance;
    private TextView tvSpeed;
    private String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        tvId = findViewById(R.id.historyTextViewId);
        tvDistance = findViewById(R.id.historyTextViewDistance);
        tvTime = findViewById(R.id.historytextViewTime);
        tvSpeed = findViewById(R.id.historyTextViewSpeed);
        userVerwaltung = new UserVerwaltungImpl(this);
        currentUser = userVerwaltung.getCurrentUserName();
        runToSave = new RunImpl(2, 20, 5, 7, new LinkedList<Coordinates>());


    }

    public void onClickButtonSave(View view){
        userVerwaltung.getCurrentUserName();
        try {
            userVerwaltung.addRun(runToSave, currentUser);
        }catch (IllegalArgumentException e){
            Toast.makeText(this, "runID"+runToSave.getId() +"vergeben", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadButtonLoad(View view){
        try {
            int id = runToSave.getId();
            testRun = userVerwaltung.loadRun(currentUser, id);
            updateGUI();
        }catch (IllegalArgumentException e){
            Toast.makeText(this, "runID"+testRun.getId() +"vergeben", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGUI(){
        tvId.setText(String.valueOf(testRun.getId()));
        tvTime.setText(String.valueOf(testRun.getTime()));
        tvDistance.setText(String.valueOf(testRun.getDistance()));
        tvSpeed.setText(String.valueOf(testRun.getSpeed()));
    }
}
