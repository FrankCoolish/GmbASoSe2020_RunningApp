package gmba.runningapp.control.userverwaltung;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.ParameterIsNullException;
import gmba.runningapp.exceptions.RunNotFoundException;
import gmba.runningapp.model.datenspeicher.classes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class UserVerwaltungImplTest {
    private Context ctx;
    private UserVerwaltung userVerwaltung;



    @Before
    public void createUserverwaltung() throws Exception{
        ctx = ApplicationProvider.getApplicationContext();
        userVerwaltung = new UserVerwaltungImpl(this.ctx);
        for(User u: userVerwaltung.getAllUsers()){
                userVerwaltung.deleteUser(u.getName());
        }
        ctx.getSharedPreferences("runningApp",0).edit().clear().apply();
    }

    /////////////////////////////////////////////////////////////
    ////////                   addUser()                 ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void addUserTest_UserIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.addUser(null);
    }

    @Test(expected = InvalidValueException.class)
    public void addUserTest_UserNameAlreadyTaken_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.addUser("Test1");
        userVerwaltung.addUser("Test1");
    }

    @Test(expected = InvalidValueException.class)
    public void addUserTest_UserNameIsNotFound_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.addUser("notFound");
    }

    @Test(expected = InvalidValueException.class)
    public void addUserTest_UserNameIsAnEmptyString_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.addUser("");
    }

    @Test
    public void addUserTest_add2User_UserListHasSize2_Succes() throws Exception{
        String name = "testUser1";
        String name2 = "testUser2";
        userVerwaltung.addUser(name);
        userVerwaltung.addUser(name2);

        String debug = userVerwaltung.getAllUsers().toString();
        assertTrue(debug,2 == userVerwaltung.getAllUsers().size());
    }

    /////////////////////////////////////////////////////////////
    ////////          setCurrentUserName()               ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void setCurrentUserNameTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.setCurrentUserName(null);
    }

    @Test
    public void setCurrentUserNameTest_Success() throws Exception {
        String name = "testUser3";
        userVerwaltung.setCurrentUserName(name);
        String actual = userVerwaltung.getCurrentUserName();
        assertEquals(name, actual);
    }

    /////////////////////////////////////////////////////////////
    ////////          getCurrentUserName()               ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void getCurrentUserNameTest_Success() throws Exception{
        String expected = "tada";
        userVerwaltung.setCurrentUserName(expected);
        String actual = userVerwaltung.getCurrentUserName();
        assertEquals(expected, actual);
    }

    @Test
    public void getCurrentUserNameTest_CurrentUserNotSet_ShouldReturn_notFound_Success() {
        ctx.getSharedPreferences("runningApp",0).edit().clear().apply();
        String actual = userVerwaltung.getCurrentUserName();
        assertEquals("notFound", actual);
    }

    /////////////////////////////////////////////////////////////
    ////////          getUser()                          ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void getUserTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.getUser(null);
    }

    @Test(expected = InvalidValueException.class)
    public void getUserTest_UserNameIs_notFound_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.getUser("testi");
    }

    @Test
    public void getUserTest_ReturnObject_UserImplclass_Success() throws Exception{
        String name = "test";
        userVerwaltung.deleteUser("test");
        userVerwaltung.addUser(name);
        User actual = userVerwaltung.getUser(name);
        assertEquals(UserImpl.class, actual.getClass());
    }

    @Test
    public void getUserTest_Check_Params_Success() throws Exception{
        String name = "testa";
        userVerwaltung.deleteUser("testa");
        userVerwaltung.addUser(name);
        User actual = userVerwaltung.getUser(name);
        assertEquals(1, actual.getId());
        assertEquals(name,actual.getName());
        assertEquals(0, actual.getAnzahlRuns());
        assertEquals(0, actual.getHistory().size());
    }


    /////////////////////////////////////////////////////////////
    ////////          getAllUsers()                       ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void getAllUsersTest_CheckListSize_Success() throws Exception{
        String name = "test";
        String name2 = "test2";
        userVerwaltung.addUser(name);
        userVerwaltung.addUser(name2);
        List<User> actual = userVerwaltung.getAllUsers();
        String debug = userVerwaltung.getAllUsers().toString();
        assertTrue(debug,2== actual.size());
    }

    /////////////////////////////////////////////////////////////
    ////////          deleteUser()                       ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void deleteUserTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception {
        userVerwaltung.deleteUser(null);
    }

    @Test(expected = InvalidValueException.class)
    public void deleteUserTest_Success_getUser_shouldThrowInvalidValueException() throws Exception {
        userVerwaltung.deleteUser("test");
        userVerwaltung.getUser("test");
    }

    /////////////////////////////////////////////////////////////
    ////////              addRun()                       ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void addRunTest_RunIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.addRun(null,"test");
    }

    @Test(expected = ParameterIsNullException.class)
    public void addRunTest_UserNameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        Run run = new RunImpl(1,1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,null);
    }

    @Test(expected = InvalidValueException.class)
    public void addRunTest_idInvalid_ShouldThrowInvalidValueException() throws Exception{
        Run run = new RunImpl(0,1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void addRunTest_timeInvalid_ShouldThrowInvalidValueException() throws Exception{
        Run run = new RunImpl(1,-1,1,1,"24-12-1980",new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void addRunTest_speedInvalid_ShouldThrowInvalidValueException() throws Exception{
        Run run = new RunImpl(1,1,1,-1,"24-12-1980",new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = InvalidValueException.class)
    public void addRunTest_distanceInvalid_ShouldThrowInvalidValueException() throws Exception{
        Run run = new RunImpl(1,1,-1,0,"24-12-1980",new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test
    public void addRunTest_checkAttributesSuccess() throws Exception {
        Run expected = new RunImpl(1, 1, 1, 1,"24-12-1980", new LinkedList<Coordinates>());
        userVerwaltung.addRun(expected, "test");
        Run actual = userVerwaltung.loadRun("test",1);
        assertEquals(expected.getId(),actual.getId());
        assertEquals(expected.getTime(),actual.getTime(),0.0);
        assertEquals(expected.getSpeed(),actual.getSpeed(),0.0);
        assertEquals(expected.getDistance(),actual.getDistance(),0.0);
    }

    /////////////////////////////////////////////////////////////
    ////////              loadRun()                      ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void loadRunTest_NameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.loadRun(null,1);
    }

    @Test(expected = InvalidValueException.class)
    public void loadRunTest_IdIsinvalid_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.loadRun("test",0);
    }

    @Test(expected = RunNotFoundException.class)
    public void loadRunTest_IdDoesNotExist_ShouldThrowRunNotFoundException() throws Exception{
        userVerwaltung.loadRun("test",89);
    }

    @Test
    public void loadRunTest_checkAttributesSuccess() throws Exception {
        Run expected = new RunImpl(1, 1, 1, 1,"24-12-1980", new LinkedList<Coordinates>());
        userVerwaltung.addRun(expected, "testa");
        Run actual = userVerwaltung.loadRun("testa", 1);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTime(), actual.getTime(), 0.0);
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.0);
        assertEquals(expected.getDistance(), actual.getDistance(), 0.0);
    }

    /////////////////////////////////////////////////////////////
    ////////              deleteRun()                    ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = ParameterIsNullException.class)
    public void deleteRunTest_NameIsNull_ShouldThrowParameterIsNullException() throws Exception{
        userVerwaltung.deleteRun(null,1);
    }

    @Test(expected = InvalidValueException.class)
    public void deleteRunTest_IdIsinvalid_ShouldThrowInvalidValueException() throws Exception{
        userVerwaltung.deleteRun("test",0);
    }

    @Test(expected = RunNotFoundException.class)
    public void deleteRunTest_IdDoesNotExist_ShouldInvalidValueException() throws Exception{
        userVerwaltung.deleteRun("test",89);
    }

    @Test(expected = RunNotFoundException.class)
    public void deleteRunTest_Succes() throws Exception{
        Run expected = new RunImpl(1, 1, 1, 1,"24-12-1980", new LinkedList<Coordinates>());
        userVerwaltung.addRun(expected, "testb");
        userVerwaltung.deleteRun("testb",expected.getId());
        userVerwaltung.loadRun("testb",expected.getId());
    }

    /////////////////////////////////////////////////////////////
    ////////              getHighestRunId()              ////////
    /////////////////////////////////////////////////////////////
    @Test(expected = ParameterIsNullException.class)
    public void getHighestRunIdTest_UsernameIsNull_ShouldThrowParamisNullException() throws Exception{
        userVerwaltung.getHighestRunId(null);
    }

    @Test
    public void getHighestRunIdTest_Success() throws Exception{
        Run expected = new RunImpl(20, 1, 1, 1,"24-12-1980", new LinkedList<Coordinates>());
        try {
            userVerwaltung.deleteRun("testa", 20);
        }catch (Exception e){
            //ignore if the run doesnot exist we dont need to delete it
        }
        userVerwaltung.addRun(expected, "testa");
        int actual = userVerwaltung.getHighestRunId("testa");
        assertEquals(20,actual);
    }

}