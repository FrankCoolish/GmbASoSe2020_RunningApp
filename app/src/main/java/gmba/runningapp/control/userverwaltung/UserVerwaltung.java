package gmba.runningapp.control.userverwaltung;

import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.User;

import java.util.List;

public interface UserVerwaltung {

   /**
    * Adds a User to the system with an unique id, 0 runs and the given name.
    * @param userName the name of the User which should be added to the System
    * @throws NullPointerException if param userName is null
    * @throws IllegalArgumentException if the name already exists or the value of name is: notFound
    */
   void addUser(String userName);

   /**
    * saves the name of the current user
    * @param name the name of the current user
    * @throws NullPointerException if param userName is null
    */
   void setCurrentUserName(String name);

   /**
    * retrieves the name of the Current user
    * @return the name of the current user as a String.
    *         if there is no current user the the name will be notFound
    */
   String getCurrentUserName();

   /**
    *loads a User to a given name
    * @param name name of the User u want to retrieve
    * @return the User with given name
    * @throws NullPointerException if param name is null
    * @throws IllegalArgumentException if the username is not found
    */
   User getUser(String name);

   /**
    * retrieves a List of all Users
    * @return a List of all registered User
    */
   List<User> getAllUsers();

   /**
    * deletes the user with the given name from the database
    * @param name the name of the user who will be deleted
    * @throws NullPointerException if param name is null
    */
   void deleteUser(String name);

   /**
    * Saves a given run in the Database and links it to the given user
    * @param run run wich will be saved
    * @param userName the of the user the given run belongs to
    * @throws NullPointerException if a param name is null
    * @throws IllegalArgumentException if an attribute of run is invalid and if the runId is already taken
    */
   void addRun(Run run ,String userName);

   /**
    * loads a run of a user with the given runId
    * @param userName the name of the user the run belongs to
    * @param runId the runId of the run
    * @return the Run with given id
    * @throws NullPointerException if a param name is null
    * @throws IllegalArgumentException if runId does not exist or is smaller than 1
    */
   Run loadRun(String userName, int runId);

   /**
    * deletes run from the given user with the given id
    * @param userName name of the user the run belongs to
    * @param runId id of the run which will be deleted
    * @throws NullPointerException if username is null
    * @throws IllegalArgumentException if run id is smaller than 1 or
    *                                  if the run id is not found
    */
   void deleteRun(String userName, int runId);
}
