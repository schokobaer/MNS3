package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.Consumer;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class LocationReportIntentService extends IntentService {

    public final static String LOCATIONREPORT_SERVICE = "locationreport_service";
    public final static String LOCATIONREPORT_INFO = "locationreport_info";

    private MozillaLocationRestClient mozillaLocationRestClient = new MozillaLocationRestClient();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LocationReportIntentService() {
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
    public void onDestroy() {
        super.onDestroy();
        Log.i(this.getClass().getName(), "Destroyed");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        newReport(new Consumer<LocationReport>() {
            @Override
            public void accept(LocationReport locationReport) {
                Intent response = new Intent();
                response.setAction(LOCATIONREPORT_SERVICE);
                Bundle bundle = new Bundle();
                bundle.putSerializable(LOCATIONREPORT_INFO, locationReport);
                response.putExtras(bundle);
                sendBroadcast(response);
            }
        });

    }

    /**
     * Calls the actual GPS location, then scans the cell towers and wifi networks
     * and calls the mozilla location service. then returns a new location report
     * @param callback
     */
    private void newReport(final Consumer<LocationReport> callback) {
        final Context ctx = this;
        // final List<CellTower> cellTowers = getCellTowers(this); //TODO Fixme
        final List<CellTower> cellTowers = getDebugCellTowers();

        getGpsLocation(new Consumer<Location>() {
            @Override
            public void accept(final Location gpsLocation) {
                getWifiNetworks(ctx, new Consumer<List<ScanResult>>() {
                    @Override
                    public void accept(final List<ScanResult> scanResults) {
                        mozillaLocationRestClient.getLocationAsync(ctx, cellTowers, scanResults, new Consumer<Location>() {
                            @Override
                            public void accept(final Location mozillaLocation) {
                                try {
                                    JSONObject input = mozillaLocationRestClient.fillBody(cellTowers, scanResults);
                                    LocationReport report = new LocationReport(mozillaLocation, gpsLocation, input);
                                    callback.accept(report);
                                } catch (JSONException e) {
                                    callback.accept(null);
                                }
                            }
                        });
                    }
                });
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

    private void getWifiNetworks(Context ctx, Consumer<List<ScanResult>> callback) {
        final WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        final WifiNetworkBroadcastReceiver wifiScanReceiver = new WifiNetworkBroadcastReceiver(callback, wifiManager);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        ctx.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (success) {
            Handler mHandler = new Handler(getMainLooper());
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10 * 1000);
                        wifiScanReceiver.cancel();
                    } catch (InterruptedException e) {

                    }
                }
            });
        } else {
            // scan failure handling
            // use old scanns
            List<ScanResult> results = wifiManager.getScanResults();
            callback.accept(results);
        }


    }

    @SuppressLint("MissingPermission")
    private void getGpsLocation(Consumer<Location> callback) {
        Handler mHandler = new Handler(getMainLooper());

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final SimpleLocationListener listener = new SimpleLocationListener(callback);
        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, listener, null);

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(60 * 1000);
                    listener.cancle();
                } catch (InterruptedException e) {

                }
            }
        });
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
            response.setAction(LOCATIONREPORT_SERVICE);
            response.putExtra(LOCATIONREPORT_INFO, location);
            ctx.sendBroadcast(response);
        }
    }

    private class WifiNetworkBroadcastReceiver extends BroadcastReceiver {

        private Consumer<List<ScanResult>> callback;
        private WifiManager wifiManager;
        private boolean complete;

        public WifiNetworkBroadcastReceiver(Consumer<List<ScanResult>> callback, WifiManager wifiManager) {
            this.callback = callback;
            this.wifiManager = wifiManager;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (complete) {
                return;
            }
            complete = true;
            List<ScanResult> results = wifiManager.getScanResults();
            callback.accept(results);
        }

        public void cancel() {
            if (complete) {
                return;
            }
            complete = true;
            callback.accept(new LinkedList<ScanResult>());
        }
    }

    private class SimpleLocationListener implements LocationListener {

        private Consumer<Location> callback;
        private boolean complete;

        public SimpleLocationListener(Consumer<Location> callback) {
            this.callback = callback;
            complete = false;
        }

        @Override
        public void onLocationChanged(final android.location.Location location) {
                    if (complete) {
                        return;
                    }
                    Location l = new Location(location.getLatitude(), location.getLongitude(), location.getAccuracy());
                    complete = true;
                    callback.accept(l);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            int ad = 0;
            ad ++;
        }

        @Override
        public void onProviderEnabled(String provider) {
            int ad = 0;
        }

        @Override
        public void onProviderDisabled(String provider) {
            int ad = 0;
        }

        public void testLocation() {
            if (complete) {
                return;
            }
            complete = true;
            Location location = new Location(48.1905858,16.3320705, 0.1d);
            callback.accept(location);
        }

        public void cancle() {
            if (complete) {
                return;
            }
            complete = true;
            callback.accept(null);
        }
    }

    private class SimpleFuture<T> implements Future<T> {

        private final CountDownLatch latch = new CountDownLatch(1);
        private boolean canceled;
        private T result;

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (isDone() || canceled) {
                return false;
            }
            canceled = true;
            put(null);
            return true;
        }

        @Override
        public boolean isCancelled() {
            return canceled;
        }

        @Override
        public boolean isDone() {
            return latch.getCount() == 0;
        }

        @Override
        public T get() throws ExecutionException, InterruptedException {
            latch.await();
            return result;
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
            if (latch.await(timeout, unit)) {
                return result;
            } else {
                throw new TimeoutException();
            }
        }

        public void put(T result) {
            if (isDone() || canceled) {
                return;
            }
            this.result = result;
            latch.countDown();
        }
    }

}
