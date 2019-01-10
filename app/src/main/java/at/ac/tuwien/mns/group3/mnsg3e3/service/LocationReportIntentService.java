package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.mns.group3.mnsg3e3.GeolocationApplication;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import java.util.List;

public class LocationReportIntentService extends IntentService {

    public final static String LOCATIONREPORT_SERVICE = "locationreport_service";
    public final static String LOCATIONREPORT_INFO = "locationreport_info";

    @Inject MozillaLocationRestClient mozillaLocationRestClient;
    @Inject NetworkScanService networkScanService;
    @Inject GpsLocationService gpsLocationService;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LocationReportIntentService() {
        super("Location IntentService");
    }

    @Override
    public void onCreate() {
        ((GeolocationApplication)getApplication()).getAppComponent().inject(this);
        super.onCreate();
        Log.i(this.getClass().getName(), "Created");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(this.getClass().getName(), "Started");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getName(), "Destroyed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // Sync
        List<CellTower> cellTowers = networkScanService.getCellTowers(this);
        List<ScanResult> wifiNetworks = networkScanService.getWifiNetworksSync(this);
        Location gpsLocation = gpsLocationService.getGpsLocationSync(this);
        Location mozillaLocation = mozillaLocationRestClient.getLocation(this, cellTowers, wifiNetworks);
        LocationReport report = null;
        try {
            JSONObject input = mozillaLocationRestClient.fillBody(cellTowers, wifiNetworks);
            report = new LocationReport(mozillaLocation, gpsLocation, input);
            Log.i(getClass().getName(), "Created Report. Difference is: " + report.getDifference());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent response = new Intent();
        response.setAction(LOCATIONREPORT_SERVICE);
        Bundle bundle = new Bundle();
        bundle.putSerializable(LOCATIONREPORT_INFO, report);
        response.putExtras(bundle);
        sendBroadcast(response);

    }

}
