package gmba.runningapp.view.runningGUI.run;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.control.rundetection.RunDetection;
import gmba.runningapp.control.rundetection.RunDetectionImpl;
import gmba.runningapp.control.runlogic.RunLogic;
import gmba.runningapp.control.runlogic.RunLogicImpl;
import gmba.runningapp.view.runningGUI.user.UserActivity;


public class RunActivity extends AppCompatActivity {
    private RunLogic runner;
    private TextView tvDauer;
    private TextView tvDistanz;
    private TextView tvIsRunnnig;
    private TextView tvRouteDist;
    private boolean update = false;
    private boolean isRunning = false;
    private boolean runDecOn = false;
    private CountDownTimer cdt;
    private RunDetection runDetection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Intent intent = getIntent();
        runDecOn = intent.getBooleanExtra(UserActivity.RUNDETECTION,true);
        if(runDecOn){
            runDetection = new RunDetectionImpl(this);
        }
        tvIsRunnnig = findViewById(R.id.runTextViewIsRunning);
        tvDauer = findViewById(R.id.runTextViewDauer);
        tvDistanz = findViewById(R.id.runTextViewDistanz);
        tvRouteDist = findViewById(R.id.runTextViewDistanz);
        tvDistanz.setText("0m");
        tvDauer.setText("00:00:00");
        runner = new RunLogicImpl(this);
        runner.startRun();
        this.update = true;

        cdt = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateGUI();
            }

            @Override
            public void onFinish() {
                intervallUpdate();
            }
        };
        cdt.start();
    }

    private void intervallUpdate(){
        runner.createRoutePoint();
        if(this.update) {
            cdt.start();
        }
    }


    public void onClickFertig(View view){
        this.update = false;
        runner.endRun();
    }

    private void updateGUI(){

        int s = runner.getTime()[0];
        int m = runner.getTime()[1];
        int h = runner.getTime()[2];
        tvDauer.setText(h+":"+m+":"+s);
        tvDistanz.setText(runner.getDistance()+"m");
        if(runDecOn){
            if(runDetection.getIsRunning()){
                tvIsRunnnig.setText("Running");
            }else{
                tvIsRunnnig.setText("not Running");
            }
        }
    }
}
