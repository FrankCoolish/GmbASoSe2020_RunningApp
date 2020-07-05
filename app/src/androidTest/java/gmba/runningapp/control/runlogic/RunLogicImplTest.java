package gmba.runningapp.control.runlogic;

import android.content.Context;
import android.location.Location;
import androidx.test.core.app.ApplicationProvider;
import gmba.runningapp.control.gps.GPSLogicImpl;
import gmba.runningapp.exceptions.InvalidValueException;
import gmba.runningapp.exceptions.NoGPSSensorException;
import gmba.runningapp.exceptions.NotEnoughLocationsException;
import gmba.runningapp.model.datenspeicher.classes.Coordinates;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RunLogicImplTest {
    private Context ctx;
    private RunLogicImpl runLogic ;

    @Before
    public void createRunLogic() throws Exception{
        ctx = ApplicationProvider.getApplicationContext();
        runLogic = new RunLogicImpl(ctx);
    }
    ////////////////////////////////////////////////////////////
    ////////          saveRun()                         ////////
    ////////////////////////////////////////////////////////////
    /*
    test possible test would be to Mock userverwaltung and to verify save run uses correct methods in userverwaltung
    @Test
    public void methodnameTest(){
    UserVerwaltung verwaltungMock = mock(Userverwaltung.class);
    runLogic.setUserverwaltung(verwaltungMock);
    runner.saveRun();
    verify(verwaltungMock).addRun();
    }
     */


    /////////////////////////////////////////////////////////////
    ////////           endRun()                          ////////
    /////////////////////////////////////////////////////////////
    /*
    test possible test would be to Mock userverwaltung and to verify save run uses correct methods in userverwaltung
    @Test
    public void methodnameTest(){
    UserVerwaltung verwaltungMock = mock(Userverwaltung.class);
    runLogic.setUserverwaltung(verwaltungMock);
    runner.endRun();
    verify(verwaltungMock).getHighestRunId();
    }
     */



    @Test
    public void endRunTest_ReturnsRunObject_Success(){
        runLogic.startRun();
        Run actual = runLogic.endRun();
        assertEquals(RunImpl.class,actual.getClass());
    }
    /////////////////////////////////////////////////////////////
    ////////          getTime                            ////////
    /////////////////////////////////////////////////////////////
    @Test(expected = InvalidValueException.class)
    public void getTimeTest_InvalidStartTimeSHould_ThrowException() throws Exception{
        runLogic.setStartTime(Long.MAX_VALUE);
        runLogic.getTime();
    }

    @Test
    public void getTimeTest_ReturnsAnIntArray_Success() throws Exception {
        runLogic.setStartTime(System.currentTimeMillis());
        int[] expected = new int[3];
        assertEquals(expected.getClass(), runLogic.getTime().getClass());
    }

    /////////////////////////////////////////////////////////////
    ////////          getDistance()                      ////////
    /////////////////////////////////////////////////////////////
    @Test
    public void getDistanceTest_InitialDistanceShouldBe0(){
        double actual = runLogic.getDistance();
        assertEquals(0.0 ,actual,0);
    }


    /////////////////////////////////////////////////////////////
    ////////           createRoutePoint()                ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void createRoutePointTest_ShouldConvert3LocationsToOnCoordinates_Success() throws Exception{
        Location loc1 = new Location("1");
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        Location loc2 = new Location("2");
        loc2.setLatitude(10);
        loc2.setLongitude(10);
        Location loc3 = new Location("3");
        loc3.setLatitude(20);
        loc3.setLongitude(20);
        List<Location> list = new LinkedList<>();
        list.add(loc1);
        list.add(loc2);
        list.add(loc3);
        runLogic.getGps().setLocationList(list);
        runLogic.createRoutePoint();
        assertEquals(3,runLogic.getAllData().size());
        assertEquals(1,runLogic.getRoute().size());
    }

    @Test
    public void createRoutePointTest_RouteSizeIs1ShouldNotCalculateDistanceSuccess() throws Exception{
        Location loc1 = new Location("1");
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        Location loc2 = new Location("2");
        loc2.setLatitude(10);
        loc2.setLongitude(10);
        Location loc3 = new Location("3");
        loc3.setLatitude(20);
        loc3.setLongitude(20);
        List<Location> list = new LinkedList<>();
        list.add(loc1);
        list.add(loc2);
        list.add(loc3);
        runLogic.getGps().setLocationList(list);
        double expected = runLogic.getDistance();
        runLogic.createRoutePoint();
        double actual = runLogic.getDistance();
        assertEquals(expected,actual,0);
    }

    @Test
    public void createRoutePointTest_shouldBuildMeanLocation_succes() throws Exception {
        Location loc1 = new Location("1");
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        Location loc2 = new Location("2");
        loc2.setLatitude(10);
        loc2.setLongitude(20);
        Location loc3 = new Location("3");
        loc3.setLatitude(20);
        loc3.setLongitude(40);
        List<Location> list = new LinkedList<>();
        list.add(loc1);
        list.add(loc2);
        list.add(loc3);
        runLogic.getGps().setLocationList(list);
        runLogic.createRoutePoint();
        Coordinates coords = runLogic.getRoute().get(0);
        double expectedLatitude = 10;
        double expectedLongitude = 20;
        assertEquals(expectedLatitude, coords.getCoordinates()[0], 0);
        assertEquals(expectedLongitude, coords.getCoordinates()[1], 0);
    }

    @Test(expected = NotEnoughLocationsException.class)
    public void createRoutePointTest_noLocationsAvailable_should_ThrowNotEnoughLocationsException() throws Exception{
        runLogic.getGps().setLocationList(new LinkedList<Location>());
        runLogic.createRoutePoint();
    }

    @Test(expected = NotEnoughLocationsException.class)
    public void createRoutePointTest_OneLocationsAvailable_should_ThrowNotEnoughLocationsException() throws Exception{
        Location loc1 = new Location("1");
        loc1.setLatitude(0);
        loc1.setLongitude(0);
        List<Location> list = new LinkedList<>();
        list.add(loc1);
        runLogic.getGps().setLocationList(list);
        runLogic.createRoutePoint();
    }

    //52.516850, 13.353654          52.514464, 13.348442
    //52.517214, 13.352766 76m         52.513931, 13.340726 550m
    // googlemaps zeigt weit aus geringere werte für die distanz
    @Test
    public void createRoutePointTest_Should_CalculateCorresct_DistanceSuccess() throws Exception{
        Location loc1 = new Location("1");
        loc1.setLatitude(52.514464);
        loc1.setLongitude(13.348442);

        Location loc2 = new Location("2");
        loc2.setLatitude(52.513931);
        loc2.setLongitude(13.340726);

        List<Location> list = new LinkedList<>();
        list.add(loc1);
        list.add(loc1);
        runLogic.getGps().setLocationList(list);
        runLogic.createRoutePoint();
        List<Location> list2 = new LinkedList<>();
        list2.add(loc2);
        list2.add(loc2);
        runLogic.getGps().setLocationList(list2);
        runLogic.createRoutePoint();
        double expected = 550;// googlemaps output
        double actual = runLogic.getDistance();
        assertEquals(expected,actual,25);
    }






}