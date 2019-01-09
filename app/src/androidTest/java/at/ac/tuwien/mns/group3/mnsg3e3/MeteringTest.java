package at.ac.tuwien.mns.group3.mnsg3e3;

import android.app.Application;
import android.app.Instrumentation;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import at.ac.tuwien.mns.group3.mnsg3e3.di.*;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.LocationReportIntentService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.inject.Inject;

import java.util.LinkedList;
import java.util.List;

import static com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@RunWith(AndroidJUnit4.class)
public class MeteringTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setup() throws JSONException {
        // Gps
        GpsLocationService gpsLocationService = Mockito.mock(GpsLocationService.class);
        when(gpsLocationService.getGpsLocationSync(any(Context.class))).thenReturn(new Location(0, 0, 1));

        // NetworkScan
        NetworkScanService networkScanService = Mockito.mock(NetworkScanService.class);
        when(networkScanService.getDebugCellTowers()).thenReturn(new LinkedList<CellTower>());
        when(networkScanService.getWifiNetworksSync(any(Context.class))).thenReturn(new LinkedList<ScanResult>());

        // MozillaLocationService
        MozillaLocationRestClient mozillaLocationRestClient = Mockito.mock(MozillaLocationRestClient.class);
        when(mozillaLocationRestClient.getLocation(any(Context.class), any(List.class), any(List.class))).thenReturn(new Location(0, 1, 1));
        when(mozillaLocationRestClient.fillBody(any(List.class), any(List.class))).thenReturn(new JSONObject());

        AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .serviceModule(new TestModule(gpsLocationService, mozillaLocationRestClient, networkScanService))
                .build();

        getApplication().setAppComponent(component);
    }

    GeolocationApplication getApplication() {
        return (GeolocationApplication) InstrumentationRegistry.getInstrumentation().getTargetContext().getApplicationContext();
    }

    @Test
    public void clickOnNewMetering() {
        activityRule.launchActivity(new Intent());
        clickOn(R.id.button1);
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
