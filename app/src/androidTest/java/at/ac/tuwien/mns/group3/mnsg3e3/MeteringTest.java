package at.ac.tuwien.mns.group3.mnsg3e3;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.v7.widget.RecyclerView;
import at.ac.tuwien.mns.group3.mnsg3e3.di.*;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;


import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

public class MeteringTest extends BaseUiTest {

    @Override
    public void setup() throws JSONException {
        // Gps
        GpsLocationService gpsLocationService = Mockito.mock(GpsLocationService.class);
        when(gpsLocationService.getGpsLocationSync(any(Context.class))).thenReturn(new Location(0, 0, 1));

        // NetworkScan
        NetworkScanService networkScanService = Mockito.mock(NetworkScanService.class);
        when(networkScanService.getCellTowers(any(Context.class))).thenReturn(new LinkedList<CellTower>());
        when(networkScanService.getWifiNetworksSync(any(Context.class))).thenReturn(new LinkedList<ScanResult>());

        // MozillaLocationService
        MozillaLocationRestClient mozillaLocationRestClient = Mockito.mock(MozillaLocationRestClient.class);
        when(mozillaLocationRestClient.getLocation(any(Context.class), any(List.class), any(List.class))).thenReturn(new Location(0, 1, 1));
        when(mozillaLocationRestClient.fillBody(any(List.class), any(List.class))).thenReturn(new JSONObject());

        super.setTestDB();
        super.setTestPrefService();

        // TestModule
        testModule.setGpsLocationService(gpsLocationService);
        testModule.setMozillaLocationRestClient(mozillaLocationRestClient);
        testModule.setNetworkScanService(networkScanService);
        testModule.setInitList(reports);

        AppComponent component = DaggerAppComponent.builder()
                .appModule(new AppModule(getApplication()))
                .serviceModule(testModule)
                .build();

        getApplication().setAppComponent(component);

        activityRule.launchActivity(new Intent(getApplication().getApplicationContext(), MainActivity.class));
    }

    @Test
    public void clickOnNewMetering() {
        RecyclerView rv = activityRule.getActivity().findViewById(R.id.recyclerview);
        Assert.assertEquals(0, rv.getAdapter().getItemCount());
        Espresso.onView(ViewMatchers.withId(R.id.button1)).perform(ViewActions.click());
        Assert.assertEquals(1, rv.getAdapter().getItemCount());
    }


}
