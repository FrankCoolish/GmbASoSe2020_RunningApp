package gmba.runningapp.view.runningGUI.start;

import androidx.test.rule.ActivityTestRule;
import gmba.runningapp.R;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class StartActivityTest {

    @Rule
    public ActivityTestRule<StartActivity> activityRule
            = new ActivityTestRule<>(StartActivity.class);


    @Test
    public void welcomeTextIsPlaced(){
        onView(withId(R.id.startWelcomeTextView))
        .check(matches(withText("Willkommen zu KulRunning!")));
    }

}