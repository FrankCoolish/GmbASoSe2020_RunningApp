package gmba.runningapp.view.runningGUI.run;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import gmba.runningapp.R;
import gmba.runningapp.control.gps.GPSLogicImpl;
import gmba.runningapp.control.gps.GPSSensor;
import gmba.runningapp.control.rundetection.AccelerometerSensor;
import gmba.runningapp.control.rundetection.RunDetectionImpl;
import gmba.runningapp.control.runlogic.RunLogic;
import gmba.runningapp.control.runlogic.RunLogicImpl;
import gmba.runningapp.exceptions.*;
import gmba.runningapp.view.runningGUI.user.UserActivity;


public class RunActivity extends AppCompatActivity {
    private RunLogic runner;
    private TextView tvDauer;
    private TextView tvDistanz;
    private TextView tvIsRunnnig;
    private boolean update = false;
    private boolean runDecOn = false;
    private CountDownTimer cdt;
    private CountDownTimer firstCdt;
    private CountDownTimer scdt;
    private RunDetectionImpl runDetection;
    private int countdown = 15;
    private boolean recordRun = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run);
        Intent intent = getIntent();
        tvIsRunnnig = findViewById(R.id.runTextViewIsRunning);
        tvDauer = findViewById(R.id.runTextViewDauer);
        tvDistanz = findViewById(R.id.runTextViewDistanz);
        tvDistanz.setText("0m");
        tvDauer.setText("00:00:00");
        setUpGlobals();
        runDecOn = intent.getBooleanExtra(UserActivity.RUNDETECTION,true);
        if(runDecOn){
            initRundDec();

        }else{
            firstCdt = createFirstCdt();
            firstCdt.start();
        }
    }

    private void intervallUpdate(){
        if(this.update) {
           doAndHandleCreateRoutepointExceptions();
           cdt.start();
        }
    }

    public void onClickFertig(View view){
        this.update = false;
        cdt.cancel();
        try{
            runner.createRoutePoint();
        }catch (NotEnoughLocationsException e){
            Toast.makeText(this,"Not Enough Locations for last route point",Toast.LENGTH_SHORT).show();
        }
        updateGUI();
        runner.endRun();
    }

    public void onClickSave(View view){
        try {
            runner.saveRun();
        } catch (ParameterIsNullException e) {
            e.printStackTrace();
            Toast.makeText(this, "Run is Null",Toast.LENGTH_SHORT).show();
        } catch (InvalidValueException e) {
            e.printStackTrace();
            Toast.makeText(this, "Finish your run first",Toast.LENGTH_SHORT).show();
        } catch (AttributeOfParamIsNullException e) {
            e.printStackTrace();
            Toast.makeText(this, "Attribute of Run is Null",Toast.LENGTH_SHORT).show();
        }
    }
    private void updateCountDownGUI(int sek){
        String sec = ""+sek;
        if(sek<10){
            sec = "0"+sek;
        }
        tvDauer.setText("-00:00:"+sec);
    }


    private void updateGUI(){
        int s = 0;
        try {
            s = runner.getTime()[0];
            int m = runner.getTime()[1];
            int h = runner.getTime()[2];
            String sec = ""+s;
            String min = ""+m;
            String hour = ""+h;
            if(s<10){
                sec = "0"+s;
            }
            if(m<10){
                min = "0"+m;
            }
            if(h<10){
                hour = "0"+h;
            }
            tvDauer.setText(hour+":"+min+":"+sec);
            //double rDistance = Math.round(runner.getRoutePointsDistance());
            tvDistanz.setText((int)runner.getDistance()+"m");
        } catch (InvalidValueException e) {
            e.printStackTrace();
            Toast.makeText(this,"time below 0 error", Toast.LENGTH_SHORT).show();
        }
        if(runDecOn){
            if(runDetection.getIsRunning()){
                tvIsRunnnig.setText("Running");
            }else{
                tvIsRunnnig.setText("not Running");
                if(recordRun){
                    this.update = false;
                    cdt.cancel();
                    try{
                        runner.createRoutePoint();
                    }catch (NotEnoughLocationsException e){
                        Toast.makeText(this, "Not Enough Locations for last route point",Toast.LENGTH_SHORT).show();
                    }
                    runner.endRun();
                    try{
                        runner.saveRun();
                        Toast.makeText(this, "Run saved",Toast.LENGTH_SHORT).show();
                    } catch (ParameterIsNullException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Run is Null",Toast.LENGTH_SHORT).show();
                    } catch (InvalidValueException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Finish your run first",Toast.LENGTH_SHORT).show();
                    } catch (AttributeOfParamIsNullException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Attribute of Run is Null",Toast.LENGTH_SHORT).show();
                    }
                    setUpGlobals();
                    initRundDec();

                }
            }
        }
    }

    //timer used to as countdown for to start the run... needed to get accurate first location(start)
    private CountDownTimer createFirstCdt(){
        CountDownTimer fcdt = new CountDownTimer(15000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdown--;
                updateCountDownGUI(countdown);
            }

            @Override
            public void onFinish() {
                doAndHandleCreateRoutepointExceptions();
                runner.startRun();
                cdt.start();
            }
        };
        return fcdt;
    }

    private CountDownTimer createRunningCdt() {
        CountDownTimer rcdt = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                updateGUI();
            }

            @Override
            public void onFinish() {
                intervallUpdate();
            }
        };
        return rcdt;
    }

    private CountDownTimer createRunningStateCdt() {
        CountDownTimer scdt = new CountDownTimer(10000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(checkRunningState()){
                    tvIsRunnnig.setText("Running");
                    runner.startRun();
                    recordRun = true;
                    cdt.start();
                    this.cancel();
                }else{
                    tvIsRunnnig.setText("not Running");
                }
            }

            @Override
            public void onFinish() {
                if(!recordRun)
                this.start();
            }
        };
        return scdt;
    }

    private boolean checkRunningState(){
        return runDetection.getIsRunning();
    }

    private void initRundDec(){
        runDetection = new RunDetectionImpl();
        AccelerometerSensor acs = new AccelerometerSensor(this, runDetection);
        runDetection.setAccSensor(acs);
        runDetection.startRunDetection();
        scdt = createRunningStateCdt();
        scdt.start();
    }

    private void setUpGlobals(){
        try {
            runner = new RunLogicImpl(this);
        } catch (NoGPSSensorException e) {
            e.printStackTrace();
            Toast.makeText(this,"GPS Error",Toast.LENGTH_SHORT).show();
        }
        recordRun = false;
        this.update = true;
        cdt = createRunningCdt();
    }

    private void doAndHandleCreateRoutepointExceptions(){
        try {
            runner.createRoutePoint();
        } catch (NotEnoughLocationsException e) {
            Toast.makeText(this,"Not Enough Locations for route point",Toast.LENGTH_SHORT).show();
        }
    }
}
