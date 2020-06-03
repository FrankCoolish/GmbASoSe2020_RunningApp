package gmba.runningapp.control.runlogic;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import gmba.runningapp.model.datenspeicher.classes.Run;
import gmba.runningapp.model.datenspeicher.classes.RunImpl;
import gmba.runningapp.model.datenspeicher.classes.User;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RunLogicImplTest {
    private Context ctx;
    private RunLogic runLogic ;

    @Before
    public void createRunLogic(){
        ctx = ApplicationProvider.getApplicationContext();
        runLogic = new RunLogicImpl(ctx);
    }
    /////////////////////////////////////////////////////////////
    ////////          saveRun()                          ////////
    /////////////////////////////////////////////////////////////

    @Test(expected = NullPointerException.class)
    public void saveRunTest_runIsNull_ShouldReturnNullPointerException(){
        runLogic.saveRun(null);
    }

    /////////////////////////////////////////////////////////////
    ////////           endRun()                          ////////
    /////////////////////////////////////////////////////////////


    @Test
    public void endRunTest_ReturnsRunObject_Success(){
        runLogic.startRun();
        Run actual = runLogic.endRun();
        assertEquals(RunImpl.class,actual.getClass());
    }

    @Test
    public void endRunTest_RunHasCorrectTimeAndDistance_Success(){

    }

    public void endRunTest_RunHasCorrect_Id_Success(){

    }




}