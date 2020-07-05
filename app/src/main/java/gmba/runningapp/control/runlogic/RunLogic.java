package gmba.runningapp.control.runlogic;

import gmba.runningapp.exceptions.AttributeOfParamIsNullException;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.NotEnoughLocationsException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.model.datenspeicher.classes.Run;

public interface RunLogic {
    /**
     * starts a run, which means the app starts with the gathering
     * of Data e.g. time.
     */
    void startRun();

    /**
     * finishes the run and stops the Gathering of Data Like GPS and Time
     * @return a Run with all the gathered Data in it.
     *
     */
    Run endRun();

    /**
     * gets the current spend Time of the current run
     * @return Array of int
     *          int[0] = seconds
     *          int[1] = minutes
     *          int[2] = hours
     * @throws InvalidValueException if the calculated time is smaller then 0
     */
    int[] getTime() throws InvalidValueException;

    /**
     * gets the current Distance of the current run
     * @return the distance in meters
     */
    double getDistance();

    /**
     * sends the finished run to the DB, where it will be saved.
     * @throws ParameterIsNullException if the given run is null or can be thrown from the DB saveRun()
     * @throws InvalidValueException from DB saveRun()
     * @throws AttributeOfParamIsNullException from DB saveRun()
     */
    void saveRun() throws ParameterIsNullException, InvalidValueException, AttributeOfParamIsNullException;

    /**
     * creates an average of the Gps Locations which were retrieved since the last call
     * and saves the average of the Latitude and Longitude as an route point ... can be used to draw the route on a map
     * and to calculate the distance
     * @throws NotEnoughLocationsException if there are less than 2 Locations
     */
    void createRoutePoint() throws NotEnoughLocationsException;


}
