package gmba.runningapp.control.rundetection;

public interface RunDetection {
    /**
     * starts the runDetection
     */
    void startRunDetection();

    /**
     * stops the runDetection
     */
    void stopRunDetection();

    /**
     * @return true if User is currently running
     *         false if User is currently not running
     */
    boolean getIsRunning();
}
