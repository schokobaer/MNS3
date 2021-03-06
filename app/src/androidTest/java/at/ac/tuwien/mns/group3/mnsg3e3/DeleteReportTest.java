package at.ac.tuwien.mns.group3.mnsg3e3;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import org.junit.Assert;
import org.junit.Test;

public class DeleteReportTest extends BaseUiTest {

    @Test
    public void deleteReport() {
        RecyclerView rv = activityRule.getActivity().findViewById(R.id.recyclerview);
        Assert.assertEquals(16, rv.getAdapter().getItemCount());
        Espresso.onView(ViewMatchers.withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, ViewActions.click()));
        Espresso.onView(ViewMatchers.withId(R.id.delete)).perform(ViewActions.click());
        Assert.assertEquals(15, rv.getAdapter().getItemCount());
    }
}
