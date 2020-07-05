package gmba.runningapp.model.datenspeicher;

import gmba.runningapp.exceptions.AttributeOfParamIsNullException;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.User;
import gmba.runningapp.exceptions.ParameterIsNullException;

import java.util.List;

/**
 * a Singleton that writes the data(e.g. User, Run) to shared preferences
 * all Files start with runningApp
 */
public interface Data {
    /**
     * Saves id, name and the number of runs
     * @param user the user which will be saved
     * @throws ParameterIsNullException if the param user or
     * @throws AttributeOfParamIsNullException if one of the attributes of the param user is null
     * @throws InvalidValueException if the id of the user which will be save is smaller than 1,
     *                              or the number of runs is smaller than 0
     *                              or the username: notFound
     *                              or the username is an empty String
     */
    void saveUserData(User user) throws ParameterIsNullException, AttributeOfParamIsNullException, InvalidValueException;

    /**
     * Loads User with given name, also loads his History of runs
     * to lad the History this method uses loadUserRun()
     *
     * @param userName the name of the User you want to be loaded
     * @return the User with all the stored Data.
     *         Missing data will be filled with following default values
     *         id = 0; count = -1; name = notFound;
     * @throws ParameterIsNullException if param userName is null
     */
    User loadUserData(String userName) throws ParameterIsNullException;

    /**
     * Deletes the Data of a give Username
     * @param userName the name of the User which will be deleted
     * @throws ParameterIsNullException if param userName is null
     */
    void deleteUserData(String userName) throws ParameterIsNullException;

    /**
     * Saves a List of Strings
     * @param userList the List of Strings which will be saved
     * @throws ParameterIsNullException if param userList is null
     */
    void saveUserList(List<String> userList) throws ParameterIsNullException;

    /**
     * loads the Userlist from sharedPreferences
     * @return List<String> with all the registered Usernames is empty if no User is registered
     */
    List<String> loadUserList();

    /**
     * saves the ids of all the runs to a given User
     * @param userName the name to which the runs belong
     * @param idList the List of runIds which will be saved
     * @throws ParameterIsNullException if userName or idList is null
     */
    void saveUserRunList(String userName, List<Integer> idList) throws ParameterIsNullException;

    /**
     * loads the ids of the for a given user
     * @param userName the name to which the runs belong
     * @return List<Integer> with all the run ids
     * @throws ParameterIsNullException if userName is null
     */
    List<Integer> loadUserRunList(String userName) throws ParameterIsNullException;

    /**
     * saves a Run with his id, speed, time, distance,
     *
     * @param run the run which will be saved
     * @param userName the user's name the run belongs to
     * @throws ParameterIsNullException if userName or run is null
     * @throws AttributeOfParamIsNullException if one or more of the attributes of the param run is null
     * @throws InvalidValueException if the attributes are not of the following size
     *         id > 0 ; time >= 0 ; speed >= 0 ; distance >= 0
     */
    void saveUserRun(Run run, String userName) throws ParameterIsNullException, AttributeOfParamIsNullException, InvalidValueException;

    /**
     * loads a Run from the shared preferences
     * @param userName the user's name the run belongs to
     * @param runId the run id which will be loaded
     * @return the Run with the given id of the given user
     *          if the run or an attribute cant be loaded
     *          the method will use the following default values:
     *          id = -1; time = -1; distance = -1; speed = -1;
     *
     * @throws ParameterIsNullException if userName is null
     * @throws InvalidValueException if runId < 1
     */
    Run loadUserRun(String userName, int runId) throws ParameterIsNullException, InvalidValueException;

    /**
     * deletes the run with given runId of the user
     *
     * @param userName the name of the user the run belongs to
     * @param runId the id of the run which will be deleted
     * @throws ParameterIsNullException if userName is null
     * @throws InvalidValueException if runId < 1
     */
    void deleteUserRun(String userName, int runId) throws ParameterIsNullException, InvalidValueException;

    /**
     * saves the name of the given userName
     * @param name string which will be saved
     * @throws ParameterIsNullException if userName is null
     */
    void setCurrentUser(String name) throws ParameterIsNullException;

    /**
     * loads the name of the current User
     *
     * @return a String which is the name of the current User
     *          if not current user is set the default value is:
     *          notFound
     */
    String getCurrentUser();
}
