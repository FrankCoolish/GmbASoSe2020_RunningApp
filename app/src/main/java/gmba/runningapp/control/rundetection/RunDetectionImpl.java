package gmba.runningapp.control.rundetection;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class RunDetectionImpl implements RunDetection, SensorEventListener {
    private SensorManager sensorManager;
    private Sensor mAccelerometer;
    private long lastTime = 0;
    private String state = "not running";
    private boolean isRunnig = false;
    private Context ctx;
    private final static String RUNNING = "running";
    private final static String NOT_RUNNING = "not running";

    public RunDetectionImpl(Context ctx){
        this.ctx = ctx;
        sensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void startRunDetection() {
        sensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void stopRunDetection() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public boolean getIsRunning() {
        return isRunnig;
    }

    @Override
    public final void onSensorChanged(SensorEvent event) {
        double x = event.values[0];
        double y = event.values[1];
        double z = event.values[2];



        double vec = Math.sqrt(Math.pow(x,2) + Math.pow(y,2) + Math.pow(z,2)) ;
        double vecwithoutgrav = (Math.floor((vec - 9.81)*100))/100 ;
        if(vecwithoutgrav >4.5){
            lastTime = event.timestamp;
        }

        if(lastTime !=0){

            long res = (event.timestamp - lastTime) / 1000000000;
            switch (state){
                case NOT_RUNNING:
                    if(vecwithoutgrav> 3.0 && res < 2) {
                        state = RUNNING;
                        isRunnig = true;
                    }
                    break;

                case RUNNING:
                    if(vecwithoutgrav <= 3.0 && res > 3) {
                        state = NOT_RUNNING;
                        isRunnig = false;
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
