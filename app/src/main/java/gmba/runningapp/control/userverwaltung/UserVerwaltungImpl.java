package gmba.runningapp.control.userverwaltung;

import android.content.Context;
import gmba.runningapp.exceptions.AttributeOfParamIsNullException;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.exceptions.RunNotFoundException;
import gmba.runningapp.model.datenspeicher.DataSingletonImpl;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.User;
import gmba.runningapp.model.datenspeicher.classes.UserImpl;

import java.util.LinkedList;
import java.util.List;

public class UserVerwaltungImpl implements UserVerwaltung {
    private DataSingletonImpl data;
    private List<String> userNamesList;
    private List<User> allUsers;
    private int highestId = 0;

    public UserVerwaltungImpl(Context ctx){
        data = DataSingletonImpl.getDataInstance(ctx);
        updateUserverwaltung();
    }

    private int getHighestUserId(){
        int id = 0;
        if(allUsers.isEmpty()){
            return 0;
        }
        for(User u : allUsers){
            if(u.getId()>id){
                id = u.getId();
            }
        }
        return id;
    }

    private List<String> loadUserList(){
        return data.loadUserList();
    }

    private LinkedList<User> loadAllUsers(){
        LinkedList<User> users = new LinkedList<>();
        for(String s: userNamesList){
            try {
                users.add(data.loadUserData(s));
            }catch (ParameterIsNullException e){
                e.printStackTrace();
            }
        }
        return users;
    }

    private void updateUserverwaltung(){
        userNamesList = loadUserList();
        allUsers = loadAllUsers();
        highestId = getHighestUserId();
    }


    @Override
    public void addUser(String userName) throws ParameterIsNullException, InvalidValueException {
        if(null == userName){
            throw new ParameterIsNullException("param userName is null");
        }
        if(userName.equals("notFound") || userName.equals("") ) {
            throw new InvalidValueException("name: notFound or an empty String is not allowed.");
        }
        if(userNamesList.contains(userName)){
            throw new InvalidValueException("Name bereits vergeben.");
        }else{
            userNamesList.add(userName);
            data.saveUserList(userNamesList);
            User newUser  = new UserImpl(highestId+1, userName, 0, new LinkedList<Run>());
            try{
                data.saveUserData(newUser);
            } catch(AttributeOfParamIsNullException e){
                //TODO: log exception
            }
            updateUserverwaltung();
        }
    }

    @Override
    public void setCurrentUserName(String name) throws ParameterIsNullException {
        if(null == name){
            throw new ParameterIsNullException("param name is null");
        }
        data.setCurrentUser(name);
    }

    @Override
    public String getCurrentUserName() {
        return data.getCurrentUser();
    }

    @Override
    public User getUser(String name) throws ParameterIsNullException, InvalidValueException {
        if(null == name){
            throw new ParameterIsNullException("param name is null");
        }
        for(User u : allUsers){
            if(u.getName().equals(name)){
                return u;
            }
        }
        throw new InvalidValueException("User not found.");
    }

    @Override
    public List<User> getAllUsers() {
        return allUsers;
    }

    @Override
    public void deleteUser(String name) throws ParameterIsNullException {
        if(null == name){
            throw new ParameterIsNullException("name is null ");
        }
        userNamesList.remove(name);
        data.saveUserList(userNamesList);
        data.deleteUserData(name);
        for(Integer i : data.loadUserRunList(name)){
            try {
                data.deleteUserRun(name, i);
            }catch (ParameterIsNullException e1){
                //TODO: log error
                e1.printStackTrace();
            }catch (InvalidValueException e2){
                //TODO: log error
                e2.printStackTrace();
            }
        }
        updateUserverwaltung();
    }

    @Override
    public void addRun(Run run, String userName) throws ParameterIsNullException, InvalidValueException, AttributeOfParamIsNullException {
        if(null== run){
            throw new ParameterIsNullException("run is null");
        }
        if(null== userName){
            throw new ParameterIsNullException("userName is null");
        }
        if(run.getId() < 1 || run.getTime() < 0 || run.getDistance() < 0 || run.getSpeed() < 0 ){
            throw new InvalidValueException("at Least 1 attribute of run is invalid ");
        }
        List<Integer> runIds = data.loadUserRunList(userName);
        if(runIds.contains(run.getId())){
            throw new InvalidValueException("Run ID vergeben");
        }
        data.saveUserRun(run, userName);
        runIds.add(run.getId());
        data.saveUserRunList(userName,runIds);
    }

    @Override
    public Run loadRun(String userName, int runId) throws ParameterIsNullException, InvalidValueException, RunNotFoundException{
        if(null == userName){
            throw new ParameterIsNullException("userName is null ");
        }
        if(runId <1){
            throw new InvalidValueException("Id smaller than 1");
        }
        if(data.loadUserRunList(userName).contains(runId)){
            return data.loadUserRun(userName,runId);
        }
        throw new RunNotFoundException("Id not found");
    }

    @Override
    public void deleteRun(String userName, int runId) throws ParameterIsNullException, InvalidValueException, RunNotFoundException {
        if(null == userName){
            throw new ParameterIsNullException("userName is null ");
        }
        if(runId <1){
            throw new InvalidValueException("Id smaller than 1");
        }
        List<Integer> runIds = data.loadUserRunList(userName);
        if(runIds.contains(runId)){
            data.deleteUserRun(userName,runId);
            runIds.remove((Integer)runId);
            data.saveUserRunList(userName,runIds);
            return;
        }
        throw new RunNotFoundException("Id: " +runId+ " not found" );
    }
    //TODO: needs testing
    public  int getHighestRunId(String userName) throws ParameterIsNullException {
        if(null == userName){
            throw new ParameterIsNullException("userName is null ");
        }
        List<Integer> runIds = data.loadUserRunList(userName);
        int id = 0;
        for(Integer i: runIds){
            if(i > id ){
              id = i;
             }
        }
         return id;
    }
}
