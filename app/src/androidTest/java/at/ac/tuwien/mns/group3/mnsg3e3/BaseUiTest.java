package at.ac.tuwien.mns.group3.mnsg3e3;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppModule;
import at.ac.tuwien.mns.group3.mnsg3e3.di.DaggerAppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.TestModule;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabaseFactory;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.PreferencesService;
import com.commonsware.cwac.saferoom.SafeHelperFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(AndroidJUnit4.class)
public abstract class BaseUiTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    protected List<Report> reports = new LinkedList<>();
    protected TestModule testModule = new TestModule();

    protected void setTestDB() {

        AppDatabaseFactory dbFactory = Mockito.mock(AppDatabaseFactory.class);
        //AppDatabase db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build();
        when(dbFactory.getDatabase(any(Context.class)))
                .thenReturn(Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build());
        testModule.setAppDatabaseFactory(dbFactory);
    }

    protected void setTestPrefService() {
        PreferencesService ps = Mockito.mock(PreferencesService.class);
        when(ps.isSecureMode(any(Activity.class))).thenReturn(false);
        testModule.setPreferencesService(ps);
    }

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

        // AppDatabaseFactory
        setTestDB();

        // PreferencesService
        setTestPrefService();

        AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .serviceModule(testModule)
                .build();

        getApplication().setAppComponent(component);


        activityRule.launchActivity(new Intent(getApplication().getApplicationContext(), MainActivity.class));

        ReportRepository repo = testModule.provideReportRepository(getApplication(), testModule.provideAppDatabaseFactory());
        for (Report r: reports) {
            repo.insert(r);
        }
        Thread.sleep(300);
    }

    GeolocationApplication getApplication() {
        return (GeolocationApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }

    @After
    public void cleanup() {
        /*ReportRepository repo = testModule.provideReportRepository(getApplication(), null);

        for (Report r: reports) {
            repo.delete(r);
        }*/

    }
}
