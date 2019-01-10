package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.util.DebugInfo;
import at.ac.tuwien.mns.group3.mnsg3e3.util.SimpleFuture;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NetworkScanService {


    @SuppressLint("MissingPermission")
    public List<CellTower> getCellTowers(Context ctx) {
        if (DebugInfo.TEST_CELL_TOWERS) {
            return getDebugCellTowers();
        }

        TelephonyManager tel_manager = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE));
        List<CellInfo> foundCellInfos = tel_manager.getAllCellInfo();
        List<CellTower> cellTowers = new LinkedList<>();
        for (CellInfo ci: foundCellInfos) {
            CellTower ct = new CellTower(ci);
            if (!cellTowers.contains(ct)) {
                cellTowers.add(ct);
            }
        }
        Log.i(getClass().getName(), "Received unique Cell Towers on Scan: " + cellTowers.size());
        return cellTowers;
    }

    private List<CellTower> getDebugCellTowers() {
        List<CellTower> towers = new LinkedList<>();
        towers.add(new CellTower(9983701, 232, 10, 2520, -60, CellTower.SignalType.WCDMA, true));
        towers.add(new CellTower(2345715, 232, 3, 13000, -90, CellTower.SignalType.WCDMA, true));
        Log.i(getClass().getName(), "Using Test Cell Towers");
        return towers;
    }

    public List<ScanResult> getWifiNetworksSync(Context ctx) {
        if (DebugInfo.TEST_WIFI_NETWORKS) {
            return new LinkedList<>();
        }

        SimpleFuture<List<ScanResult>> future = new SimpleFuture<>();
        WifiManager wifiManager = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        WifiNetworkBroadcastReceiver wifiScanReceiver = new WifiNetworkBroadcastReceiver(future, wifiManager);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        ctx.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            // scan failure handling
            // use old scans
            Log.i(getClass().getName(), "Using old scan results for wifiNetworkScan");
            return wifiManager.getScanResults();
        }

        try {
            List<ScanResult> results = future.get(15, TimeUnit.SECONDS);
            Log.i(getClass().getName(), "Received results on WifiNetworkScan: " + results.size());
            ctx.unregisterReceiver(wifiScanReceiver);
            return results;
        } catch (ExecutionException | TimeoutException | InterruptedException e) {
            Log.w(getClass().getName(), "Timeout in getWifiNetworks");
            return new LinkedList<>();
        }
    }

    private class WifiNetworkBroadcastReceiver extends BroadcastReceiver {

        private SimpleFuture<List<ScanResult>> future;
        private WifiManager wifiManager;
        private boolean complete;

        public WifiNetworkBroadcastReceiver(SimpleFuture<List<ScanResult>> future, WifiManager wifiManager) {
            this.future = future;
            this.wifiManager = wifiManager;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (complete) {
                return;
            }
            complete = true;
            List<ScanResult> results = wifiManager.getScanResults();
            future.put(results);
        }

    }
}
