package at.ac.tuwien.mns.group3.mnsg3e3;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import org.junit.Test;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist;
import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;

public class DisplayReportsTest extends BaseUiTest {

    @Test
    public void displayAndScrollOnReportList() {
        String assertionString = "Location: ooo";
        assertNotExist(assertionString);
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview)).perform(RecyclerViewActions.scrollToPosition(12));
        assertDisplayed(assertionString);
    }
}
