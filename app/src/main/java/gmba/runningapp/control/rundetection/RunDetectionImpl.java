package gmba.runningapp.control.rundetection;

import gmba.runningapp.exceptions.InvalidValueException;

public class RunDetectionImpl implements RunDetection {
    private AccelerometerSensor accSensor;
    private long lastTime = 0;
    private boolean isRunnig = false;
    private final static String RUNNING = "running";
    private final static String NOT_RUNNING = "not running";
    private String runningState = NOT_RUNNING;

    public RunDetectionImpl(){
    }

    public void setAccSensor(AccelerometerSensor accSensor) {
        this.accSensor = accSensor;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public void startRunDetection() {
        accSensor.startSensor();
    }

    @Override
    public void stopRunDetection() {
        accSensor.stopSensor();
    }

    @Override
    public boolean getIsRunning() {
        return isRunnig;
    }

    @Override
    public void detectRunningState( double vector, long timedif) throws InvalidValueException {
        if(vector <0 || timedif <0){
            throw new InvalidValueException("vector or timedifferenz is below 0");
        }
        if(lastTime !=0){
            switch (runningState){
                case NOT_RUNNING:
                    if(vector> 3.0 && timedif < 2) {
                        runningState = RUNNING;
                        isRunnig = true;
                    }
                    break;
                case RUNNING:
                    if(vector <= 3.0&& timedif > 3) {
                        runningState = NOT_RUNNING;
                        isRunnig = false;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    protected void stampTime(double vec, long time){
        if(vec > 3.0){
            lastTime = time;
        }
    }

    protected long calcTimediff(long nowTime){
        long time = (nowTime - lastTime) / 1000000000;
        return  time;
    }

    protected double calculateVectorWithOutGravity(double xValue, double yValue, double zValue){
        double vec = Math.sqrt(Math.pow(xValue,2) + Math.pow(yValue,2) + Math.pow(zValue,2)) ;
        double vecwithoutgrav = (Math.floor((vec - 9.81)*100))/100 ;
        return  vecwithoutgrav;
    }
}
