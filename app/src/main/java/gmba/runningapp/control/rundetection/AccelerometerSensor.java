package gmba.runningapp.control.rundetection;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import gmba.runningapp.exceptions.InvalidValueException;

public class AccelerometerSensor implements SensorEventListener {
    private final SensorManager sensorManager;
    private final Sensor mAccelerometer;
    private final RunDetectionImpl runDetection;

    public AccelerometerSensor(Context ctx, RunDetectionImpl rdc){
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.runDetection = rdc;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        sendToRunDetection(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void startSensor(){
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stopSensor(){
        sensorManager.unregisterListener(this);
    }

    private void sendToRunDetection(SensorEvent event){
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];
        long time = event.timestamp;

        try {
            double vec = runDetection.calculateVectorWithOutGravity(x,y,z);
            runDetection.detectRunningState(vec,runDetection.calcTimediff(time));
            runDetection.stampTime(vec,time);
        } catch (InvalidValueException e) {
            e.printStackTrace();
        }
    }
}
