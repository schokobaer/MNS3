package at.ac.tuwien.mns.group3.mnsg3e3;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import org.junit.Test;

import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed;
import static com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertNotExist;

public class OpenReportTest extends BaseUiTest {

    @Test
    public void openReportTest() throws InterruptedException {
        String precision = String.format("%-20s: %s", "Precision", 99.0d);
        Thread.sleep(3 * 1000);
        assertNotExist(precision);
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        Thread.sleep(5 * 1000);

        assertDisplayed(precision);

    }
}
