package gmba.runningapp.model.datenspeicher;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.test.core.app.ApplicationProvider;
import gmba.runningapp.exceptions.AttributeOfParamIsNullException;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.model.datenspeicher.classes.*;
import org.junit.Test;

import java.util.*;


import static org.junit.Assert.*;

public class DataSingletonImplTest {
    private final Context ctx = ApplicationProvider.getApplicationContext();
    private final DataSingletonImpl data = DataSingletonImpl.getDataInstance(ctx);
    private static final String FILENAME = "runningApp";
    private static final String USER_ID = "userId";
    private static final String USER_RUN_COUNT = "userRunCount";
    private static final String USER_NAME = "userName";
    private static final String USER_LIST = "userList";
    private static final String USER_RUN_IDS = "userRunID";
    private static final String RUN_ID = "runId";
    private static final String RUN_TIME = "runTime";
    private static final String RUN_DISTANCE = "runDistance";
    private static final String RUN_COORDS = "runCoords";
    private static final String RUN_SPEED = "runSpeed";
    private static final String CURRENT_USER = "currentUser";


    /////////////////////////////////////////////////////////////
    ////////              saveUserData()                 ////////
    /////////////////////////////////////////////////////////////


    @Test(expected = ParameterIsNullException.class)
    public void saveUserDataTest_UserIsNull_ShouldThrowParameterIsNullException() throws Exception {
        data.saveUserData(null);
    }

    @Test(expected = AttributeOfParamIsNullException.class)
    public void saveUserDataTest_UserNameIsNull_ShouldReturnAttributeOfParamIsNullException() throws Exception {
        User user = new UserImpl(1,null,0, new LinkedList<Run>());
        data.saveUserData(user);
    }

    @Test(expected = AttributeOfParamIsNullException.class)
    public void saveUserDataTest_UserHistoryIsNull_ShouldAttributeOfParamIsNullException() throws Exception {
        User user = new UserImpl(1,"test",0, null);
        data.saveUserData(user);
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserDataTest_UserIdIsSmallerThen1_ShouldReturnInvalidValueException() throws Exception {
        User user = new UserImpl(0,"test",0, new LinkedList<Run>());
        data.saveUserData(user);
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserDataTest_UsernameIsNotFound_ShouldReturnInvalidValueException() throws Exception {
        User user = new UserImpl(1,"notFound",-1, new LinkedList<Run>());
        data.saveUserData(user);
    }
    @Test(expected = InvalidValueException.class)
    public void saveUserDataTest_UserUsernameIsAnEmptyString_ShouldReturnInvalidValueException() throws Exception {
        User user = new UserImpl(1,"",-1, new LinkedList<Run>());
        data.saveUserData(user);
    }

    @Test
    public void saveUserDataTest_UserId_is100_Success()  throws Exception {
        User user = new UserImpl(100,"test",0, new LinkedList<Run>());
        data.saveUserData(user);
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME +"_User_"+user.getName(),Context.MODE_PRIVATE);
        int id = sharedPref.getInt(USER_ID, -1);
        assertEquals(100,id);
    }

    @Test
    public void saveUserDataTest_UserName_IsTest_Success() throws Exception {
        User user = new UserImpl(1,"test",0, new LinkedList<Run>());
        data.saveUserData(user);
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME +"_User_"+user.getName(),Context.MODE_PRIVATE);
        String actual = sharedPref.getString(USER_NAME, "no");
        assertEquals("test",actual);
    }

    @Test
    public void saveUserDataTest_UserCount_is2_Success() throws Exception {
        List<Run> runs = new LinkedList();
        for(int i = 0; i<77;i++){
            runs.add(null);
        }
        User user = new UserImpl(1,"test",77, runs);
        data.saveUserData(user);
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME +"_User_"+user.getName(),Context.MODE_PRIVATE);
        int actual = sharedPref.getInt(USER_RUN_COUNT, -1);
        assertEquals(77,actual);
    }

    /////////////////////////////////////////////////////////////
    ////////              loadUserData()                  ///////
    /////////////////////////////////////////////////////////////


    @Test(expected = ParameterIsNullException.class)
    public void loadUserDataTest_ParamUsernameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        data.loadUserData(null);
    }

    @Test
    public void loadUserDataTest_ReturnsUserImplClass_Success() throws Exception {
        User actual = data.loadUserData("test");
        assertEquals(UserImpl.class,actual.getClass());
    }

    @Test
    public void loadUserDataTest_CheckResultDefaultValues_Success() throws Exception {
        ctx.getSharedPreferences(FILENAME+"_User_test1",0).edit().clear().apply();
        User actual = data.loadUserData("test1");
        assertEquals(0,actual.getId());
        assertEquals(-1,actual.getAnzahlRuns());
        assertEquals("notFound",actual.getName());
    }

    @Test
    public void loadUserDataTest_UserValues_Success() throws Exception {
        List<Run> runs = new LinkedList();
        for(int i = 0; i<22;i++){
            runs.add(null);
        }
        User expected = new UserImpl(10,"Someone",22,runs);
        data.saveUserData(expected);
        User actual = data.loadUserData("Someone");
        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getAnzahlRuns(),actual.getAnzahlRuns());
        assertEquals(expected.getName(),actual.getName());
    }

    /////////////////////////////////////////////////////////////
    ////////            deleteUserData()                  ///////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void deleteUserDataTest_ParamUsernameIsNull_ShouldThrowParamIsNullException() throws Exception {
        data.deleteUserData(null);
    }

    /*
    after deleting the data of a given User, the User which is returned with loadUserData should have
    default values
     */
    @Test
    public void deleteUserDataTest_Success() throws Exception {
        User user = new UserImpl(1,"test",3, new LinkedList<Run>());
        data.saveUserData(user);
        data.deleteUserData(user.getName());
        User actual = data.loadUserData(user.getName());
        assertEquals(0,actual.getId());
        assertEquals(-1,actual.getAnzahlRuns());
        assertEquals("notFound",actual.getName());
    }

    /////////////////////////////////////////////////////////////
    ////////              saveUserList()                 ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void addUserListTest_UserListIsNull_ShouldThrowParameterIsNullException() throws Exception{
        data.saveUserList(null);
    }

    @Test
    public void saveUserListTest_Success() throws Exception {
        List<String> expected = new LinkedList<>();
        expected.add("test");
        expected.add("something");
        data.saveUserList(expected);
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        Set<String> actual = sharedPref.getStringSet(USER_LIST, new HashSet<String>());
        assertEquals(expected.size(),actual.size());
        assertTrue(actual.contains("test"));
        assertTrue(actual.contains("something"));
    }

    /////////////////////////////////////////////////////////////
    ////////              loadUserList()                 ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void loadUserListTest_Success() throws Exception{
        List<String> expected = new LinkedList<>();
        expected.add("newTest");
        expected.add("newSomething");
        expected.add("AnotherOne");
        data.saveUserList(expected);
        List<String> actual = data.loadUserList();
        assertEquals(expected.size(),actual.size());
        assertTrue( actual.contains("newTest"));
        assertTrue( actual.contains("newSomething"));
        assertTrue( actual.contains("AnotherOne"));
    }

    /////////////////////////////////////////////////////////////
    ////////             saveUserRunList()               ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void saveUserRunListTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception {
        data.saveUserRunList(null, new LinkedList<Integer>());
    }

    @Test(expected = ParameterIsNullException.class)
    public void saveUserRunListTest_idListIsNull_ShouldThrowParameterIsNullException() throws Exception {
        data.saveUserRunList("something",null);
    }

    @Test
    public void saveUserRunListTest_Success() throws Exception {
        List<Integer> ids = new LinkedList<>();
        ids.add(1);
        ids.add(9);
        ids.add(101);
        data.saveUserRunList("testUser",ids);
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(FILENAME+"_User_"+"testUser",Context.MODE_PRIVATE);
        Set<String> actual = sharedPreferences.getStringSet(USER_RUN_IDS, new HashSet<String>());
        assertEquals(ids.size(), actual.size());
        assertTrue("loaded List doesnt not contain 1",actual.contains(String.valueOf(1)));
        assertTrue("loaded List doesnt not contain 9",actual.contains(String.valueOf(9)));
        assertTrue("loaded List doesnt not contain 101",actual.contains(String.valueOf(101)));
    }

    /////////////////////////////////////////////////////////////
    ////////             loadUserRunList()               ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void loadUserRunListTest_userNameIsNull_ShouldThrowParameterIsNullException() throws Exception {
        data.loadUserRunList(null);
    }

    @Test
    public void loadUserRunListTest_UserDoesnotExist_ShouldReturnemptyList_Success() throws Exception {
        ctx.getSharedPreferences(FILENAME+"_User_testing",0).edit().clear().apply();
        List<Integer> actual = data.loadUserRunList("testing");
        assertTrue("retruned List should be Empty",actual.isEmpty());
    }

    @Test
    public void loadUserRunListTest_Success() throws Exception {
        List<Integer> ids = new LinkedList<>();
        ids.add(1);
        ids.add(9);
        ids.add(101);
        data.saveUserRunList("testUser",ids);
        List<Integer> actual = data.loadUserRunList("testUser");
        assertEquals(ids.size(), actual.size());
        assertTrue("loaded List doesnt not contain 1",actual.contains(1));
        assertTrue("loaded List doesnt not contain 9",actual.contains(9));
        assertTrue("loaded List doesnt not contain 101",actual.contains(101));
    }

    /////////////////////////////////////////////////////////////
    ////////             saveUserRun()                   ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void saveUserRunTest_runIsNull_ShouldThrowParameterIsNullException()throws Exception{
        data.saveUserRun(null,"test");
    }

    @Test(expected = ParameterIsNullException.class)
    public void saveUserRunTest_userNameIsNull_ShouldThrowParameterIsNullException()throws Exception{
        Run run = new RunImpl(1,1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(run,null);
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserRunTest_runIDIs0_ShouldThrowInvalidValueException()throws Exception{
        Run run = new RunImpl(0,1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserRunTest_runTimeIs0_ShouldThrowInvalidValueException()throws Exception{
        Run run = new RunImpl(1,-1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserRunTest_runDistanceIs0_ShouldThrowInvalidValueException()throws Exception{
        Run run = new RunImpl(1,1,-1,1,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void saveUserRunTest_runSpeedIs0_ShouldThrowInvalidValueException()throws Exception{
        Run run = new RunImpl(1,1,1,-1,"24-12-1980", new LinkedList<Coordinates>());
        data.saveUserRun(run,"test");
    }

    @Test(expected = AttributeOfParamIsNullException.class)
    public void saveUserRunTest_DateIsNull_ShouldThrowAttributeOfParamIsNullException()throws Exception{
        Run run = new RunImpl(1,1,1,-1,null, new LinkedList<Coordinates>());
        data.saveUserRun(run,"test");
    }

    @Test
    public void saveUserRunTest_Success()throws Exception{
        Run run = new RunImpl(1,1,1,1,"24-12-1980", new LinkedList<Coordinates>());
        data.saveUserRun(run,"testUser");
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME+"_User_"+"testUser"+"_RunId_"+1,Context.MODE_PRIVATE);
        assertEquals(run.getId(), sharedPref.getInt(RUN_ID,-1));
        assertEquals(run.getTime(), sharedPref.getFloat(RUN_TIME,-1),0.0);
        assertEquals(run.getDistance(), sharedPref.getFloat(RUN_DISTANCE,-1),0.0);
        assertEquals(run.getSpeed(), sharedPref.getFloat(RUN_SPEED,-1),0.0);
        assertEquals(run.getCoordinates().size(), sharedPref.getStringSet(RUN_COORDS,new LinkedHashSet<String>()).size());
    }

    /////////////////////////////////////////////////////////////
    ////////             saveRunCoordsSuccess()          ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void saveRunCoordsSuccessTest_success()throws Exception{
        Coordinates c = new CoordinatesImpl(15.123456, 20.1234567);
        Coordinates c2 = new CoordinatesImpl(14.123456, 21.1234567);
        List<Coordinates> coords = new LinkedList<>();
        coords.add(c);
        coords.add(c2);
        Run run = new RunImpl(1,1,1,1,"24-12-1980",coords);
        data.saveUserRun(run, "testUser");
        Run loadedRun = data.loadUserRun("testUser", 1);
        List<Coordinates> loadedCoords = loadedRun.getCoordinates();
        assertEquals(2,loadedCoords.size());
        assertEquals(15.123456, loadedCoords.get(0).getCoordinates()[0],0);
        assertEquals(20.1234567, loadedCoords.get(0).getCoordinates()[1],0);
        assertEquals(14.123456, loadedCoords.get(1).getCoordinates()[0],0);
        assertEquals(21.1234567, loadedCoords.get(1).getCoordinates()[1],0);
    }

    /////////////////////////////////////////////////////////////
    ////////             loadUserRun()                   ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = InvalidValueException.class)
    public void loadUserRunTest_runIDIsNull_ShouldThrowInvalidValueException()throws Exception{
        data.loadUserRun("test",0);
    }

    @Test(expected = ParameterIsNullException.class)
    public void loadUserRunTest_UserNameIsNull_ShouldThrowParameterIsNullException()throws Exception{
        data.loadUserRun(null,1);
    }

    @Test
    public void loadUserRunTest_CheckRunAttributes_success()throws Exception{
        Run expected = new RunImpl(1,1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(expected,"testUser");
        Run actual = data.loadUserRun("testUser", expected.getId());
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTime(), actual.getTime(),0.0);
        assertEquals(expected.getSpeed(),actual.getSpeed(), 0.0);
        assertEquals(expected.getDistance(),actual.getDistance(),0.0);
    }

    @Test
    public void loadUserRunTest_CheckDefaultValues_success()throws Exception{
        ctx.getSharedPreferences(FILENAME+"_User_"+"testUser"+"_RunId_"+1,0).edit().clear().apply();
        Run actual = data.loadUserRun("testUser", 1);
        assertEquals(-1, actual.getId());
        assertEquals(-1, actual.getTime(),0.0);
        assertEquals(-1,actual.getSpeed(), 0.0);
        assertEquals(-1,actual.getDistance(),0.0);
    }

    /////////////////////////////////////////////////////////////
    ////////             deleteUserRun()                 ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = InvalidValueException.class)
    public void deleteUserRunTest_runIDIsNull_ShouldThrowInvalidValueException()throws Exception{
        data.deleteUserRun("test",0);
    }

    @Test(expected = ParameterIsNullException.class)
    public void deleteUserRunTest_UserNameIsNull_ShouldThrowParameterIsNullException()throws Exception{
        data.deleteUserRun(null,1);
    }

    @Test
    public void deleteUserRunTest_Success()throws Exception{
        Run run = new RunImpl(2,2,2,2,"24-12-1980",new LinkedList<Coordinates>());
        data.saveUserRun(run,"testUser");
        ctx.getSharedPreferences(FILENAME+"_User_"+"testUser"+"_RunId_"+2,0).edit().clear().apply();
        Run actual = data.loadUserRun("testUser", 2);
        assertEquals(-1, actual.getId());
        assertEquals(-1, actual.getTime(),0.0);
        assertEquals(-1,actual.getSpeed(), 0.0);
        assertEquals(-1,actual.getDistance(),0.0);
    }

    /////////////////////////////////////////////////////////////
    ////////             setCurrentUser()                ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void setCurrentUserTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception {
        data.setCurrentUser(null);
    }

    @Test
    public void setCurrentUserTest_Success() throws Exception{
        String expected = "Test";
        data.setCurrentUser(expected);
        SharedPreferences sharedPref = ctx.getSharedPreferences(FILENAME,Context.MODE_PRIVATE);
        String actual = sharedPref.getString(CURRENT_USER, "default");
        assertEquals(expected,actual);
    }

    /////////////////////////////////////////////////////////////
    ////////             getCurrentUser()                ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void getCurrentUserTest_Success() throws Exception{
        String expected = "Test";
        data.setCurrentUser(expected);
        String actual = data.getCurrentUser();
        assertEquals(expected,actual);
    }

    @Test
    public void getCurrentUserTest_checkDefaultValue_Success() throws Exception{
        String username = "Test2";
        data.setCurrentUser(username);
        ctx.getSharedPreferences(FILENAME,0).edit().clear().apply();
        String actual = data.getCurrentUser();
        assertEquals("notFound",actual);
    }

}