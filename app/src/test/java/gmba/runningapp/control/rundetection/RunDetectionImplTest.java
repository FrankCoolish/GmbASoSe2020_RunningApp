package gmba.runningapp.control.rundetection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RunDetectionImplTest {
    RunDetectionImpl rdc = new RunDetectionImpl();
    AccelerometerSensor acsMock = mock(AccelerometerSensor.class);

    @Before
    public void setup(){
        rdc.setAccSensor(acsMock);
        rdc.setLastTime(0);
    }

    /////////////////////////////////////////////////////////////
    ////////             startRunDetection()             ////////
    /////////////////////////////////////////////////////////////
    @Test
    public void startRunDetectionTest_verifySensorStarts_Success(){
        rdc.startRunDetection();
        verify(acsMock).startSensor();
    }

    /////////////////////////////////////////////////////////////
    ////////             stopRunDetection()              ////////
    /////////////////////////////////////////////////////////////
    @Test
    public void stopRunDetectionTest_VerifySensorStops_Success(){
        rdc.stopRunDetection();
        verify(acsMock).stopSensor();
    }

    /////////////////////////////////////////////////////////////
    ////////             getIsRunning()                  ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void getIsRunningTest_initialValue_ShouldReturn_False_Success(){
        Boolean actual = rdc.getIsRunning();
        assertEquals(false, actual);
    }


    /////////////////////////////////////////////////////////////
    ////////             detectRunningState()            ////////
    /////////////////////////////////////////////////////////////

    @Test
    public void detectRunningStateTest_IsNotRunningChangetoRunning_Success()throws  Exception{
        rdc.setLastTime(1);
        assertEquals(false,rdc.getIsRunning());
        rdc.detectRunningState(3.01,1);
        assertEquals(true,rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_IsNotRunning_TimeDiffIsToHigh_Should_notChange()throws Exception{
        rdc.setLastTime(1);
        assertEquals(false,rdc.getIsRunning());
        rdc.detectRunningState(3.01,2);
        assertEquals(false,rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_IsNotRunning_VectorIsToSmall_Should_notChange()throws Exception{
        rdc.setLastTime(1);
        assertEquals(false,rdc.getIsRunning());
        rdc.detectRunningState(3.0,1);
        assertEquals(false,rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_IsRunning_Should_Change_IsNotRunning_Success()throws Exception{
        rdc.setLastTime(1);
        rdc.detectRunningState(3.01,1);
        assertEquals(true,rdc.getIsRunning());
        rdc.detectRunningState(3.00,4);
        assertEquals(false, rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_IsRunning_VectorToHigh_Should_NotChange()throws Exception{
        rdc.setLastTime(1);
        rdc.detectRunningState(3.01,1);
        assertEquals(true,rdc.getIsRunning());
        rdc.detectRunningState(3.01,4);
        assertEquals(true, rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_IsRunning_TimeToSmall_Should_NotChange()throws Exception{
        rdc.setLastTime(1);
        rdc.detectRunningState(3.01,1);
        assertEquals(true,rdc.getIsRunning());
        rdc.detectRunningState(3.00,3);
        assertEquals(true, rdc.getIsRunning());
    }

    @Test
    public void detectRunningStateTest_lastTimeIs0_should_notChange()throws Exception{
        rdc.setLastTime(0);
        assertEquals(false,rdc.getIsRunning());
        rdc.detectRunningState(3.01,1);
        assertEquals(false,rdc.getIsRunning());
    }

}