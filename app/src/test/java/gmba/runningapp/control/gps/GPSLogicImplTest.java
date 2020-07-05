package gmba.runningapp.control.gps;

import android.location.Location;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;


import static org.mockito.Mockito.verify;

public class GPSLogicImplTest {
    GPSLogicImpl gps = new GPSLogicImpl();
    GPSSensor sensorMock = mock(GPSSensor.class);


    public GPSLogicImplTest(){
        List locList = new LinkedList();
        gps.setLocationList(locList);
    }

    @Before
    public void setup(){
        gps.setSensor(sensorMock);
        gps.getLocationList().clear();
    }


    /////////////////////////////////////////////////////////////
    ////////             retrieveLocationList            ////////
    /////////////////////////////////////////////////////////////
    @Test
    public void retrieveLocationListTest_ListHasOneLocation_should_returnEmptyList_Success(){
        List l = gps.getLocationList();
        gps.getLocationList().add(new Location(""));
        List actual = gps.retrieveLocationList();
        assertEquals(actual.size(),0);
        assertEquals(l.size(), 1);
    }

    @Test
    public void retrieveLocationListTest_return2LocationAndClearsList__Success(){
        List l = gps.getLocationList();
        gps.getLocationList().add(new Location("1"));
        gps.getLocationList().add(new Location("2"));
        List actual = gps.retrieveLocationList();
        assertEquals(actual.size(),2);
        assertEquals(l.size(), 0);
    }
    @Test
    public void retrieveLocationListTest_gpsLocationsAreEmpty_returnsEmptyList(){
        List l = gps.getLocationList();
        List actual = gps.retrieveLocationList();
        assertEquals(actual.size(),0);
        assertEquals(l.size(), 0);
    }



    /////////////////////////////////////////////////////////////
    ////////             startLocationUpdates            ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void startLocationUpdatesTest_VerifySensorStartLocationUpdatesIsTriggered()throws Exception{
        gps.startLocationUpdates();
        verify(sensorMock).startLocationUpdates();
    }

    /////////////////////////////////////////////////////////////
    ////////             stopLocationUpdates             ////////
    /////////////////////////////////////////////////////////////
    @Test
    public void stopLocationUpdatesTest_VerifySensorStopLocationUpdatesisTriggered()throws Exception{
        gps.stopLocationUpdates();
        verify(sensorMock).stopLocationUpdates();
    }
}