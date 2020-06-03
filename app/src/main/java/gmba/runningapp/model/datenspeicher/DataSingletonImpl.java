package gmba.runningapp.model.datenspeicher;

import android.content.Context;
import android.content.SharedPreferences;
import gmba.runningapp.model.datenspeicher.classes.*;


import java.util.*;

public class DataSingletonImpl implements Data {
    private static Context ctx;
    private static DataSingletonImpl instance;
    private static final String FILENAME = "runningApp";
    private static final String CURRENT_USER = "currentUser";
    private static final String USER_LIST = "userList";
    private static final String USER_ID = "userId";
    private static final String USER_RUN_COUNT = "userRunCount";
    private static final String USER_NAME = "userName";
    private static final String USER_RUN_IDS = "userRunID";
    private static final String RUN_ID = "runId";
    private static final String RUN_TIME = "runTime";
    private static final String RUN_DISTANCE = "runDistance";
    private static final String RUN_COORDS = "runCoords";
    private static final String RUN_SPEED = "runSpeed";

    private DataSingletonImpl() {
    }

    public static DataSingletonImpl getDataInstance(Context ctx){
        if(DataSingletonImpl.instance == null) {
            DataSingletonImpl.instance = new DataSingletonImpl();
        }
        DataSingletonImpl.ctx = ctx;
        return DataSingletonImpl.instance;
    }

    @Override
    public void saveUserData(User user) throws NullPointerException, IllegalArgumentException {
        if(null == user){
            throw new NullPointerException("user is null");
        }
        if(null == user.getName() || null == user.getName() || null == user.getHistory()){
            throw new NullPointerException("an attribute of User is null");
        }
        int id = user.getId();
        int count = user.getAnzahlRuns();
        if(id <= 0 || count <0){
            throw new IllegalArgumentException("id should be greater than 0 and minimum count should be 0");
        }
        String name = user.getName();
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(USER_ID,id);
        editor.putString(USER_NAME, name);
        editor.putInt(USER_RUN_COUNT, count);
        editor.apply();
    }

    @Override
    public User loadUserData(String userName) throws NullPointerException {
        if( null == userName){
            throw new NullPointerException("param userName is null");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName,Context.MODE_PRIVATE);
        int id = sharedPref.getInt(USER_ID, 0);
        String name = sharedPref.getString(USER_NAME,"notFound");
        int count = sharedPref.getInt(USER_RUN_COUNT,-1);
        return new UserImpl(id, name, count, new LinkedList<Run>());
    }

    @Override
    public void deleteUserData(String userName) throws NullPointerException {
        if( null == userName){
            throw new NullPointerException("param userName is null");
        }
        ctx.getSharedPreferences(FILENAME+"_User_"+userName,0).edit().clear().apply();
    }

    @Override
    public void saveUserList(List<String> userList) throws NullPointerException {
        if( null == userList){
            throw new NullPointerException("param userList is null");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> saveSet = new HashSet<>(userList);
        editor.putStringSet(USER_LIST, saveSet);
        editor.apply();
    }

    @Override
    public List<String> loadUserList() {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        Set<String> loadedSet = sharedPref.getStringSet(USER_LIST, new HashSet<String>());
        return new LinkedList<>(loadedSet);
    }

    @Override
    public void saveUserRunList(String userName, List<Integer> idList) throws NullPointerException {
        if( null == userName || null == idList){
            throw new NullPointerException("a param is null");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName,Context.MODE_PRIVATE);
        Set<String> saveSet = new HashSet<>();
        SharedPreferences.Editor editor = sharedPref.edit();
        for(Integer i : idList){
            saveSet.add(String.valueOf(i));
        }
        editor.putStringSet(USER_RUN_IDS,saveSet);
        editor.apply();
    }

    @Override
    public List<Integer> loadUserRunList(String userName) throws NullPointerException {
        if( null == userName ){
            throw new NullPointerException("param userName is null");
        }
        List<Integer> idList = new LinkedList<>();
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName,Context.MODE_PRIVATE);
        Set<String> loadedSet = new HashSet<>();
        loadedSet.addAll(sharedPref.getStringSet(USER_RUN_IDS, new HashSet<String>()));
        for(String s : loadedSet){
            idList.add(Integer.valueOf(s));
        }
        return idList;
    }

    @Override
    public void saveUserRun(Run run, String userName) throws NullPointerException, IllegalArgumentException {
        if( null == userName || null == run ){
            throw new NullPointerException("a param is null");
        }
        int id = run.getId();
        float time = run.getTime();
        float distance = run.getDistance();
        float speed = run.getSpeed();
        if(id < 1 || time < 0 || distance < 0 || speed < 0 ){
            throw new IllegalArgumentException("at Least 1 attribute of run is invalid ");
        }
        Set<String> coords = new HashSet<>();
        for(Coordinates c: run.getCoordinates()){
            double[] d = c.getCoordinates();
            coords.add(String.valueOf(d[0]));
            coords.add(String.valueOf(d[1]));
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+id,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(RUN_ID,id);
        editor.putFloat(RUN_TIME, time);
        editor.putFloat(RUN_DISTANCE,distance);
        editor.putStringSet(RUN_COORDS, coords);
        editor.putFloat(RUN_SPEED, speed);
        editor.apply();
    }

    @Override
    public Run loadUserRun(String userName, int runId ) throws NullPointerException, IllegalArgumentException {
        if(null == userName){
            throw new NullPointerException("userName is null");
        }
        if(1 > runId){
            throw new IllegalArgumentException("runId has invaled value (<1)");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+runId,Context.MODE_PRIVATE);
        int id = sharedPref.getInt(RUN_ID, -1);
        float speed = sharedPref.getFloat(RUN_SPEED, -1);
        float distance = sharedPref.getFloat(RUN_DISTANCE, -1);
        float time = sharedPref.getFloat(RUN_TIME, -1) ;
        Set<String> stringCoords = sharedPref.getStringSet(RUN_COORDS, new HashSet<String>());
        List<Coordinates> coords = convertCoordinatesToList(stringCoords);
        Run r = new RunImpl(id,time,distance,speed,coords);
        return r;
    }

    @Override
    public void deleteUserRun(String userName, int runId) throws NullPointerException, IllegalArgumentException {
        if(null == userName){
            throw new NullPointerException("userName is null");
        }
        if(1 > runId){
            throw new IllegalArgumentException("runId has invaled value (<1)");
        }
        ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+runId,0).edit().clear().apply();
    }

    private LinkedList<Coordinates> convertCoordinatesToList(Set<String> stringCoords){
        LinkedList<Coordinates> coords = new LinkedList<>();
        /*
        double[] d = new double[2];
        int count = 1;
        for(String s : stringCoords ){

        }
        */

        return coords;
    }


    @Override
    public void setCurrentUser(String name) throws NullPointerException {
        if(null == name){
            throw new NullPointerException("name is null");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(CURRENT_USER, name);
        editor.apply();
    }

    @Override
    public String getCurrentUser() {
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        return sharedPref.getString(CURRENT_USER, "notFound");
    }
}
