package at.ac.tuwien.mns.group3.mnsg3e3.service;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.v4.util.Consumer;
import android.util.Log;
import at.ac.tuwien.mns.group3.mnsg3e3.model.CellTower;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MozillaLocationRestClient {

    public Location getLocation(Context ctx, List<CellTower> cellTowers, List<ScanResult> wifiNetworks) {

        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "https://location.services.mozilla.com/v1/geolocate?key=test";

        JSONObject body;

        try {
            body = fillBody(cellTowers, wifiNetworks);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, body, future, future);

        queue.add(req);
        queue.start();

        try {
            JSONObject response = future.get();
            if (response.has("error")) {
                Log.w(getClass().getName(), "Received an error on the REST call");
                return null;
            }
            JSONObject jsonLocation = response.getJSONObject("location");
            Location location = new Location(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"), response.getDouble("accuracy"));
            Log.i(getClass().getName(), "Received Location from REST call: " + location.toString());
            return location;
        } catch (Exception e) {
            return null;
        }
    }

    public void getLocationAsync(Context ctx, List<CellTower> cellTowers, List<ScanResult> wifiNetworks, final Consumer<Location> callback) {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        String url = "https://location.services.mozilla.com/v1/geolocate?key=test";

        JSONObject body;

        try {
            body = fillBody(cellTowers, wifiNetworks);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.accept(null);
            return;
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response.has("error")) {
                    callback.accept(null);
                }
                try {
                    JSONObject jsonLocation = response.getJSONObject("location");
                    Location location = new Location(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng"), response.getDouble("accuracy"));
                    callback.accept(location);
                } catch (JSONException e) {
                    e.printStackTrace();
                    callback.accept(null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.accept(null);
            }
        });

        queue.add(req);
        queue.start();
    }

    public JSONObject fillBody(List<CellTower> cellTowers, List<ScanResult> wifiNetworks) throws JSONException {
        JSONObject body = new JSONObject();

        // Add Cell Towers
        JSONArray jsonCellTowers = new JSONArray();
        for (CellTower t: cellTowers) {
            JSONObject jsonTower = new JSONObject();
            jsonTower.put("radioType", t.getSignalType().name().toLowerCase());
            jsonTower.put("mobileCountryCode", t.getCountryCode());
            jsonTower.put("mobileNetworkCode", t.getNetId());
            jsonTower.put("locationAreaCode", t.getAreaCode());
            jsonTower.put("cellId", t.getCellId());
            jsonTower.put("signalStrength", t.getSignalStrength());
            jsonCellTowers.put(jsonTower);
        }
        body.put("cellTowers", jsonCellTowers);

        // Add WiFi Networks
        JSONArray jsonWifiNetworks = new JSONArray();
        for (ScanResult wifi: wifiNetworks) {
            JSONObject jsonWifi = new JSONObject();
            if (wifi.BSSID != null && !wifi.BSSID.isEmpty()) {
                jsonWifi.put("macAddress", wifi.BSSID);
            }
            if (wifi.channelWidth != 0) {
                jsonWifi.put("channel", wifi.channelWidth);
            }
            if (wifi.frequency != 0) {
                jsonWifi.put("frequency", wifi.frequency);
            }
            if (wifi.level != 0) {
                jsonWifi.put("signalStrength", wifi.level);
            }
            jsonWifiNetworks.put(jsonWifi);
        }
        body.put("wifiAccessPoints", jsonWifiNetworks);

        return body;
    }
}
