package gmba.runningapp.model.datenspeicher;

import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.User;

import java.util.List;

/**
 * a Singleton that writes the data(e.g. User, Run) to shared preferences
 * all Files start with runningApp
 */
public interface Data {
    /**
     * Saves id, name and the number of runs
     * @param user the user which will be saved
     * @throws NullPointerException if the param user or one of his attributes are null
     * @throws IllegalArgumentException if the id of the user which will be save is smaller than 1
     *
     * TODO: save History of the User, right now it History is not saved in this method
     */
    void saveUserData(User user);

    /**
     * Loads User with given name
     * @param userName the name of the User you want to be loaded
     * @return the User with all the stored Data.
     *         Missing data will be filled with following default values
     *         id = 0; count = -1; name = notFound;
     *
     * @throws NullPointerException if param userName is null
     *
     * TODO: handle History, as for now the Users History is empty LinkedList<Run>
     */
    User loadUserData(String userName);

    /**
     * Deletes the Data of a give Username
     * @param userName the name of the User which will be deleted
     * @throws NullPointerException if param userName is null
     */
    void deleteUserData(String userName);

    /**
     * Saves a List of Strings
     * @param userList the List of Strings which will be saved
     * @throws NullPointerException if param userList is null
     */
    void saveUserList(List<String> userList);

    /**
     * loads the Userlist from sharedPreferences
     * @return List<String> with all the registered Usernames is empty if no User is registered
     */
    List<String> loadUserList();

    /**
     * saves the ids of all the runs to a given User
     * @param userName the name to which the runs belong
     * @param idList the List of runIds which will be saved
     * @throws NullPointerException if userName or idList is null
     */
    void saveUserRunList(String userName, List<Integer> idList);

    /**
     * loads the ids of the for a given user
     * @param userName the name to which the runs belong
     * @return List<Integer> with all the run ids
     * @throws NullPointerException if userName is null
     */
    List<Integer> loadUserRunList(String userName);

    /**
     * saves a Run with his id, speed, time, distance, TODO: and coords
     *
     * @param run the run which will be saved
     * @param userName the user's name the run belongs to
     * @throws NullPointerException if userName or run is null
     * @throws IllegalArgumentException if the attributes are not of the following size
     *         id > 0 ; time >= 0 ; speed >= 0 ; distance >= 0
     */
    void saveUserRun(Run run, String userName);

    /**
     * loads a Run from the shared preferences
     * @param userName the user's name the run belongs to
     * @param runId the run id which will be loaded
     * @return the Run with the given id of the given user
     *          if the run or an attribute cant be loaded
     *          the method will use the following default values:
     *          id = -1; time = -1; distance = -1; speed = -1;
     *
     * @throws NullPointerException if userName is null
     * @throws IllegalArgumentException if runId < 1
     */
    Run loadUserRun(String userName, int runId);

    /**
     * deletes the run with given runId of the user
     *
     * @param userName the name of the user the run belongs to
     * @param runId the id of the run which will be deleted
     * @throws NullPointerException if userName is null
     * @throws IllegalArgumentException if runId < 1
     */
    void deleteUserRun(String userName, int runId);

    /**
     * saves the name of the given userName
     * @param name string which will be saved
     * @throws NullPointerException if userName is null
     */
    void setCurrentUser(String name);

    /**
     * loads the name of the current User
     *
     * @return a String which is the name of the current User
     *          if not current user is set the default value is:
     *          notFound
     */
    String getCurrentUser();
}
