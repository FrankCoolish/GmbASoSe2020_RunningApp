package gmba.runningapp.view.runningGUI.start;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import gmba.runningapp.R;
import gmba.runningapp.view.runningGUI.history.MapsActivity;
import gmba.runningapp.view.runningGUI.history.MyRunListRecyclerViewAdapter;
import gmba.runningapp.view.runningGUI.history.RunListActivity;
import gmba.runningapp.view.runningGUI.run.RunActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.*;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.*;


@RunWith(AndroidJUnit4.class)
public class RunListActivityIntentTest {

    private static final int RUNID = 1;
    private static final String PACKAGE_NAME = "gmba.runningapp";

    /* Instantiate an IntentsTestRule object. */
    @Rule
    public IntentsTestRule<RunListActivity> intentsRule =
            new IntentsTestRule(RunListActivity.class);

    // schlägt fehl
    public void verifyRunIdSentToMapsActivity() {

        RunListActivity activity = intentsRule.getActivity();
        activity.dataforEsspressoTest();
        onView(withId(R.id.rvRunList))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(allOf(
                hasComponent(hasShortClassName(".view.runningGUI.history.MapsActivity")),
                toPackage(PACKAGE_NAME),
                hasExtra(RunListActivity.SELECTED_RUN_MESSAGE, RUNID)));

    }

}
