package gmba.runningapp.control.runlogic;

import gmba.runningapp.model.datenspeicher.classes.Run;

public interface RunLogic {
    /**
     * starts a run, which means the app starts with the gathering
     * of necessary data like GPS and TIME.
     */
    void startRun();

    /**
     * finishes the run and stops the Gathering of Data Like GPS and Time
     * @return a Run with all the gathered Data in it.
     */
    Run endRun();

    /**
     * gets the current spend Time of the current run
     * @return Array of int
     *          int[0] = seconds
     *          int[1] = minutes
     *          int[2] = hours
     */
    int[] getTime();

    /**
     * gets the current Distance of the current run
     * @return the distance in meters
     */
    int getDistance();

    /**
     * sends the finished run to the DB, where it will be saved.
     * @throws NullPointerException if run is null
     */
    void saveRun(Run run);

    /**
     * creates an average of the Gps Locations which were retrieved in the last 30 seconds
     * and saves the average as an route point ... can be used to draw the route on a map
     */
    void createRoutePoint();
}
