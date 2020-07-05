package gmba.runningapp.control.rundetection;

import gmba.runningapp.exceptions.InvalidValueException;

public interface RunDetection {

    /**
     * detects whether someone is running or not. the result can be obtained via getIsRunning()
     * @param vec the force which pressure on the device excluding acceleration of gravity
     * @param timedif the time between the now and the last time the vec was over 3.5 ins seconds
     * @throws InvalidValueException if one of the params are below 0
     */
    void detectRunningState(double vec, long timedif) throws InvalidValueException;

    /**
     * starts the runDetection
     * starts the gatering of Data by Sensor
     */
    void startRunDetection();

    /**
     * stops the runDetection
     * stops the Data gathering by the Sensor
     */
    void stopRunDetection();

    /**
     * @return true if User is currently running
     *         false if User is currently not running
     */
    boolean getIsRunning();
}
