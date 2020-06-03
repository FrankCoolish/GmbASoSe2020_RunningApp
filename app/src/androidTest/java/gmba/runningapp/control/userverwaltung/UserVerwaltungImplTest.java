package gmba.runningapp.control.userverwaltung;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import gmba.runningapp.model.datenspeicher.DataSingletonImpl;
import gmba.runningapp.model.datenspeicher.classes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class UserVerwaltungImplTest {
    private Context ctx;
    private UserVerwaltung userVerwaltung;

    /////////////////////////////////////////////////////////////
    ////////                   addUser()                 ////////
    /////////////////////////////////////////////////////////////

    @Before
    public void createUserverwaltung(){
        ctx = ApplicationProvider.getApplicationContext();
        userVerwaltung = new UserVerwaltungImpl(this.ctx);
        for(User u: userVerwaltung.getAllUsers()){
            userVerwaltung.deleteUser(u.getName());
        }
        ctx.getSharedPreferences("runningApp",0).edit().clear().apply();
    }

    @Test(expected = NullPointerException.class)
    public void addUserTest_UserIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.addUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserTest_UserNameAlreadyTaken_ShouldThrowIllegalArgumentException(){
        userVerwaltung.addUser("Test1");
        userVerwaltung.addUser("Test1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addUserTest_UserNameIsNotFound_ShouldThrowIllegalArgumentException(){
        userVerwaltung.addUser("notFound");
    }

    @Test
    public void addUserTest_add2User_UserListHasSize2_Succes(){
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

    @Test(expected = NullPointerException.class)
    public void setCurrentUserNameTest_UserNameIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.setCurrentUserName(null);
    }

    @Test
    public void setCurrentUserNameTest_Success(){
        String name = "testUser3";
        userVerwaltung.setCurrentUserName(name);
        String actual = userVerwaltung.getCurrentUserName();
        assertEquals(name, actual);
        /*for(User u: userVerwaltung.getAllUsers()){
            if(u.getName().equals(name)){
                actual = u.getName();
            }
        }
         */
    }

    /////////////////////////////////////////////////////////////
    ////////          getCurrentUserName()               ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void getCurrentUserNameTest_Success(){
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

    @Test(expected = NullPointerException.class)
    public void getUserTest_UserNameIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.getUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getUserTest_UserNameIs_notFound_ShouldThrowIllegalArgumentException(){
        userVerwaltung.getUser("testi");
    }

    @Test
    public void getUserTest_ReturnObject_UserImplclass_Success(){
        String name = "test";
        userVerwaltung.deleteUser("test");
        userVerwaltung.addUser(name);
        User actual = userVerwaltung.getUser(name);
        assertEquals(UserImpl.class, actual.getClass());
    }

    @Test
    public void getUserTest_Check_Params_Success(){
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
    public void getAllUsersTest_CheckListSize_Success(){
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

    @Test(expected = NullPointerException.class)
    public void deleteUserTest_UserNameIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.deleteUser(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteUserTest_Success_getUser_shouldThrowIllegalArgumentException(){
        userVerwaltung.deleteUser("test");
        userVerwaltung.getUser("test");
    }

    /////////////////////////////////////////////////////////////
    ////////              addRun()                       ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = NullPointerException.class)
    public void addRunTest_RunIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.addRun(null,"test");
    }

    @Test(expected = NullPointerException.class)
    public void addRunTest_UserNameIsNull_ShouldThrowNullPointerException(){
        Run run = new RunImpl(1,1,1,1,new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRunTest_idInvalid_ShouldThrowIllegalArgumentException(){
        Run run = new RunImpl(0,1,1,1,new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRunTest_timeInvalid_ShouldThrowIllegalArgumentException(){
        Run run = new RunImpl(1,-1,1,1,new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRunTest_speedInvalid_ShouldThrowIllegalArgumentException(){
        Run run = new RunImpl(1,1,1,-1,new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addRunTest_distanceInvalid_ShouldThrowIllegalArgumentException(){
        Run run = new RunImpl(1,1,-1,0,new LinkedList<Coordinates>());
        userVerwaltung.addRun(run,"test");
    }

    @Test
    public void addRunTest_checkAttributesSuccess() {
        Run expected = new RunImpl(1, 1, 1, 1, new LinkedList<Coordinates>());
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

    @Test(expected = NullPointerException.class)
    public void loadRunTest_NameIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.loadRun(null,1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadRunTest_IdIsinvalid_ShouldThrowIlliegalArgumentException(){
        userVerwaltung.loadRun("test",0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void loadRunTest_IdDoesNotExist_ShouldThrowIlliegalArgumentException(){
        userVerwaltung.loadRun("test",89);
    }

    @Test
    public void loadRunTest_checkAttributesSuccess() {
        Run expected = new RunImpl(1, 1, 1, 1, new LinkedList<Coordinates>());
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

    @Test(expected = NullPointerException.class)
    public void deleteRunTest_NameIsNull_ShouldThrowNullPointerException(){
        userVerwaltung.deleteRun(null,1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRunTest_IdIsinvalid_ShouldThrowIlliegalArgumentException(){
        userVerwaltung.deleteRun("test",0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRunTest_IdDoesNotExist_ShouldThrowIlliegalArgumentException(){
        userVerwaltung.deleteRun("test",89);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteRunTest_Succes(){
        Run expected = new RunImpl(1, 1, 1, 1, new LinkedList<Coordinates>());
        userVerwaltung.addRun(expected, "testb");
        userVerwaltung.deleteRun("testb",expected.getId());
        userVerwaltung.loadRun("testb",expected.getId());
    }
}