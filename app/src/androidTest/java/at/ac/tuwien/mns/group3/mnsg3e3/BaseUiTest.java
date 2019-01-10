package at.ac.tuwien.mns.group3.mnsg3e3;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppModule;
import at.ac.tuwien.mns.group3.mnsg3e3.di.DaggerAppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.TestModule;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public abstract class BaseUiTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    protected List<Report> reports = new LinkedList<>();
    private TestModule testModule = new TestModule();;

    @Before
    public void setup() throws Exception {
        // Repo
        reports.add(new Report("Heute", "ABC", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Morgen", "LALI", 99, "ASDF", "QWERT", 5));
        reports.add(new Report("Gestern", "Testloc", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Dezember", "wwww", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Montag", "poli", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Juni", "GPSLocation", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Mittelalter", "KDKDK", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Wasserzeit", "WUEUW", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Heute2", "ABC2", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Morgen2", "LALI2", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Gestern2", "Testloc2", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Dezember2", "qqqqqq", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Montag2", "ooo", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Juni2", "GPSLocation2", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Mittelalter2", "KDKDK2", 1, "ASDF", "QWERT", 5));
        reports.add(new Report("Wasserzeit2", "WUEUW2", 1, "ASDF", "QWERT", 5));

        // TestModule
        testModule.setInitList(reports);

        AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .serviceModule(testModule)
                .build();

        getApplication().setAppComponent(component);


        activityRule.launchActivity(new Intent(getApplication().getApplicationContext(), MainActivity.class));
    }

    GeolocationApplication getApplication() {
        return (GeolocationApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }

    @After
    public void cleanup() {
        ReportRepository repo = testModule.provideReportRepository(testModule.provideAppDatabase(getApplication()));

        for (Report r: reports) {
            repo.delete(r);
        }

    }
}
