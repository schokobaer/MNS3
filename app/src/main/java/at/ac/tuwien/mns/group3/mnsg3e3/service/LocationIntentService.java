package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.util.Consumer;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;

import java.util.LinkedList;
import java.util.List;

public class LocationIntentService extends IntentService {

    public final static String MOZILLA_LOCATION_INFO = "mozilla_location_info";
    public final static String LOCATION_INFO = "location_info";

    private MozillaLocationRestClient mozillaLocationRestClient = new MozillaLocationRestClient();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LocationIntentService() {
        super("Location IntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getName(), "Created");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.i(this.getClass().getName(), "Started");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        final Context ctx = this;
        // final List<CellTower> cellTowers = getCellTowers(this);
        final List<CellTower> cellTowers = getDebugCellTowers();
        getWifiNetworks(ctx, new Consumer<List<ScanResult>>() {
            @Override
            public void accept(List<ScanResult> scanResults) {
                new BackgroundWorker(ctx, cellTowers, scanResults).execute();
            }
        });

    }

    @SuppressLint("MissingPermission")
    private List<CellTower> getCellTowers(Context ctx) {
        TelephonyManager tel_manager = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE));
        List<CellInfo> foundCellInfos = tel_manager.getAllCellInfo();
        List<CellTower> cellTowers = new LinkedList<>();
        for (CellInfo ci: foundCellInfos) {
            CellTower ct = new CellTower(ci);
            if (!cellTowers.contains(ct)) {
                cellTowers.add(ct);
            }
        }
        return cellTowers;
    }

    private List<CellTower> getDebugCellTowers() {
        List<CellTower> towers = new LinkedList<>();
        towers.add(new CellTower(9983701, 232, 10, 2520, -60, CellTower.SignalType.WCDMA, true));
        towers.add(new CellTower(2345715, 232, 3, 13000, -90, CellTower.SignalType.WCDMA, true));
        return towers;
    }

    private void getDebugWifiNetworks(Context ctx, Consumer<List<ScanResult>> callback) {
        callback.accept(new LinkedList<ScanResult>());
    }

    private void getWifiNetworks(Context ctx, final Consumer<List<ScanResult>> callback) {
        final WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false);
                List<ScanResult> results = wifiManager.getScanResults();
                callback.accept(results);
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        ctx.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            // use old scanns
            List<ScanResult> results = wifiManager.getScanResults();
            callback.accept(results);
        }


    }


    private class BackgroundWorker extends AsyncTask<Void, Void, Location> {

        private Context ctx;
        private List<CellTower> cellTowers;
        private List<ScanResult> wifiNetworks;

        BackgroundWorker(Context ctx, List<CellTower> cellTowers, List<ScanResult> wifiNetworks) {
            this.ctx = ctx;
            this.cellTowers = cellTowers;
            this.wifiNetworks = wifiNetworks;
        }

        @Override
        protected Location doInBackground(Void... voids) {
            return mozillaLocationRestClient.getLocation(ctx, cellTowers, wifiNetworks);
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            Intent response = new Intent();
            response.setAction(MOZILLA_LOCATION_INFO);
            response.putExtra(LOCATION_INFO, location);
            ctx.sendBroadcast(response);
        }
    }

}
