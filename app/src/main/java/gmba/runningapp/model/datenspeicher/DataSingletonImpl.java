package gmba.runningapp.model.datenspeicher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import gmba.runningapp.exceptions.AttributeOfParamIsNullException;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
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
    private static final String RUN_DATE = "runDate";

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
    public void saveUserData(User user) throws ParameterIsNullException, InvalidValueException, AttributeOfParamIsNullException {
        if(null == user){
            throw new ParameterIsNullException("user is null");
        }
        if(null == user.getName() || null == user.getName() || null == user.getHistory()){
            throw new AttributeOfParamIsNullException("an attribute of User is null");
        }
        if(user.getName().equals("notFound")){
            throw new InvalidValueException("user name: notFound is not allowed");
        }
        if(user.getName().equals("")){
            throw new InvalidValueException("an emptyString as username is not allowed");
        }
        int id = user.getId();
        int count = user.getHistory().size();
        if(id <= 0 ){
            throw new InvalidValueException("id should be greater than 0 and minimum count should be 0");
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
    public User loadUserData(String userName) throws ParameterIsNullException {
        if( null == userName){
            throw new ParameterIsNullException("param userName is null");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName,Context.MODE_PRIVATE);
        int id = sharedPref.getInt(USER_ID, 0);
        String name = sharedPref.getString(USER_NAME,"notFound");
        int count = sharedPref.getInt(USER_RUN_COUNT,-1);

        List<Run> history = new LinkedList<>();
        for(int i: loadUserRunList(userName)){
            try {
                history.add(loadUserRun(userName, i));
            }catch (InvalidValueException e){
                //TODO: log error
            }
        }
        return new UserImpl(id, name, count, history);
    }

    @Override
    public void deleteUserData(String userName) throws ParameterIsNullException {
        if( null == userName){
            throw new ParameterIsNullException("param userName is null");
        }
        ctx.getSharedPreferences(FILENAME+"_User_"+userName,0).edit().clear().apply();
    }

    @Override
    public void saveUserList(List<String> userList) throws ParameterIsNullException {
        if( null == userList){
            throw new ParameterIsNullException("param userList is null");
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
    public void saveUserRunList(String userName, List<Integer> idList) throws ParameterIsNullException {
        if( null == userName || null == idList){
            throw new ParameterIsNullException("a param is null");
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
    public List<Integer> loadUserRunList(String userName) throws ParameterIsNullException {
        if( null == userName ){
            throw new ParameterIsNullException("param userName is null");
        }
        List<Integer> idList = new LinkedList<>();
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName,Context.MODE_PRIVATE);
        Set<String> loadedSet = new HashSet<>(sharedPref.getStringSet(USER_RUN_IDS, new HashSet<String>()));
        for(String s : loadedSet){
            idList.add(Integer.valueOf(s));
        }
        return idList;
    }

    @Override
    public void saveUserRun(Run run, String userName) throws ParameterIsNullException, InvalidValueException, AttributeOfParamIsNullException {
        if( null == userName || null == run ){
            throw new ParameterIsNullException("a param is null");
        }
        if(null == run.getDate())
            throw new AttributeOfParamIsNullException("attribute Date of run is null");
        int id = run.getId();
        float time = run.getTime();
        float distance = run.getDistance();
        float speed = run.getSpeed();
        String date = run.getDate();
        if(id < 1 || time < 0 || distance < 0 || speed < 0 ){
            throw new InvalidValueException("at Least 1 attribute of run is invalid ");
        }
        Set<String> coords = convertCoordinatesToStringSet(run.getCoordinates());
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+id,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(RUN_ID,id);
        editor.putFloat(RUN_TIME, time);
        editor.putFloat(RUN_DISTANCE,distance);
        editor.putStringSet(RUN_COORDS, coords);
        editor.putFloat(RUN_SPEED, speed);
        editor.putString(RUN_DATE, date);
        editor.apply();
    }

    @Override
    public Run loadUserRun(String userName, int runId ) throws ParameterIsNullException, InvalidValueException{
        if(null == userName){
            throw new ParameterIsNullException("userName is null");
        }
        if(1 > runId){
            throw new InvalidValueException("runId has invaled value: <1");
        }
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+runId,Context.MODE_PRIVATE);
        int id = sharedPref.getInt(RUN_ID, -1);
        float speed = sharedPref.getFloat(RUN_SPEED, -1);
        float distance = sharedPref.getFloat(RUN_DISTANCE, -1);
        float time = sharedPref.getFloat(RUN_TIME, -1) ;
        String date = sharedPref.getString(RUN_DATE,"noDate");
        Set<String> stringCoords = sharedPref.getStringSet(RUN_COORDS, new HashSet<String>());
        ArrayList<Coordinates> coords = convertStringsToCoordinates(stringCoords);
        return new RunImpl(id,time,distance,speed,date,coords);
    }

    @Override
    public void deleteUserRun(String userName, int runId) throws ParameterIsNullException, InvalidValueException {
        if(null == userName){
            throw new ParameterIsNullException("userName is null");
        }
        if(1 > runId){
            throw new InvalidValueException("runId has invaled value (<1)");
        }
        ctx.getSharedPreferences(FILENAME+"_User_"+userName+"_RunId_"+runId,0).edit().clear().apply();
    }

    private Set<String>  convertCoordinatesToStringSet(List<Coordinates> coords){
        Set<String> stringCoords = new HashSet<>();
        int order = 0 ;
        for(Coordinates c  : coords){
            double lat = c.getCoordinates()[0];
            double lo = c.getCoordinates()[1];
            String setEntry = order+";"+lat+","+lo;
            stringCoords.add(setEntry);
            order++;
        }
        return stringCoords;
    }

    private ArrayList<Coordinates> convertStringsToCoordinates(Set<String> stringSet){
        ArrayList<Coordinates> coords = new ArrayList<>();
        for(int i =0; i< stringSet.size(); i++){
            coords.add(null);
        }
        for(String s: stringSet){
            int divider = s.indexOf(",");
            int orderDivider = s.indexOf(";");
            if(divider >= 1 && s.length() > divider) {
                int order = Integer.parseInt(s.substring(0,orderDivider));
                double lat = Double.parseDouble(s.substring(orderDivider+1, divider));
                double lo = Double.parseDouble(s.substring(divider+1));
                Coordinates coordinates = new CoordinatesImpl(lat,lo);
                coords.set(order,coordinates);
            }
        }
        return coords;
    }


    @Override
    public void setCurrentUser(String name) throws ParameterIsNullException {
        if(null == name){
            throw new ParameterIsNullException("name is null");
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
