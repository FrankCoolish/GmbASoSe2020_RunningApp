package gmba.runningapp.control.userverwaltung;

import android.content.Context;
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
            users.add(data.loadUserData(s));
        }
        return users;
    }

    private void updateUserverwaltung(){
        userNamesList = loadUserList();
        allUsers = loadAllUsers();
        highestId = getHighestUserId();
    }


    @Override
    public void addUser(String userName) throws IllegalArgumentException, NullPointerException {
        if(null == userName){
            throw new NullPointerException("param userName is null");
        }
        if(userName.equals("notFound")) {
            throw new IllegalArgumentException("name notFound is not allowed.");
        }
        if(userNamesList.contains(userName)){
            throw new IllegalArgumentException("Name bereits vergeben.");
        }else{
            userNamesList.add(userName);
            data.saveUserList(userNamesList);
            User newUser  = new UserImpl(highestId+1, userName, 0, new LinkedList<Run>());
            data.saveUserData(newUser);
            updateUserverwaltung();
        }
    }

    @Override
    public void setCurrentUserName(String name) throws NullPointerException {
        if(null == name){
            throw new NullPointerException("param name is null");
        }
        data.setCurrentUser(name);
    }

    @Override
    public String getCurrentUserName() {
        return data.getCurrentUser();
    }

    @Override
    public User getUser(String name) throws IllegalArgumentException, NullPointerException {
        if(null == name){
            throw new NullPointerException("param name is null");
        }
        for(User u : allUsers){
            if(u.getName().equals(name)){
                return u;
            }
        }
        throw new IllegalArgumentException("User not found.");
    }

    @Override
    public List<User> getAllUsers() {
        return allUsers;
    }

    @Override
    public void deleteUser(String name) {
        if(null == name){
            throw new NullPointerException("name is null ");
        }
        userNamesList.remove(name);
        data.saveUserList(userNamesList);
        data.deleteUserData(name);
        for(Integer i : data.loadUserRunList(name)){
            data.deleteUserRun(name,i);
        }
        updateUserverwaltung();
    }

    @Override
    public void addRun(Run run, String userName) throws NullPointerException, IllegalArgumentException {
        if(null== run){
            throw new NullPointerException("run is null");
        }
        if(null== userName){
            throw new NullPointerException("userName is null");
        }
        if(run.getId() < 1 || run.getTime() < 0 || run.getDistance() < 0 || run.getSpeed() < 0 ){
            throw new IllegalArgumentException("at Least 1 attribute of run is invalid ");
        }
        List<Integer> runIds = data.loadUserRunList(userName);
        if(runIds.contains(run.getId())){
            throw new IllegalArgumentException("Run ID vergeben");
        }
        data.saveUserRun(run, userName);
        runIds.add(run.getId());
        data.saveUserRunList(userName,runIds);
    }

    @Override
    public Run loadRun(String userName, int runId) throws NullPointerException, IllegalArgumentException {
        if(null == userName){
            throw new NullPointerException("userName is null ");
        }
        if(runId <1){
            throw new IllegalArgumentException("Id smaller than 1");
        }
        if(data.loadUserRunList(userName).contains(runId)){
            return data.loadUserRun(userName,runId);
        }
        throw new IllegalArgumentException("Id not found");
    }

    @Override
    public void deleteRun(String userName, int runId) {
        if(null == userName){
            throw new NullPointerException("userName is null ");
        }
        if(runId <1){
            throw new IllegalArgumentException("Id smaller than 1");
        }
        if(data.loadUserRunList(userName).contains(runId)){
            data.deleteUserRun(userName,runId);
        }
        throw new IllegalArgumentException("Id not found");
    }

    private int getHighestRunId(List<Integer> runs){
      int id = 0;
      for(Integer i: runs){
          if(i < id ){
              id = i;
          }
      }
      return id;
    };


}
