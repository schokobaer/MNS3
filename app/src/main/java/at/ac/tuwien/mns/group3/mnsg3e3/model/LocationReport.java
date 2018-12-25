package at.ac.tuwien.mns.group3.mnsg3e3.model;

import android.os.Parcelable;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

public class LocationReport implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date timestamp;
    private Location mozillaLocation;
    private Location gpsLocation;
    private String mozillaInput;

    public LocationReport(Location mozillaLocation, Location gpsLocation, JSONObject mozillaInput) {
        timestamp = new Date();
        this.mozillaLocation = mozillaLocation;
        this.gpsLocation = gpsLocation;
        this.mozillaInput = mozillaInput.toString();
    }

    public double getDifference() {
        if (mozillaLocation == null || gpsLocation == null) {
            return 0;
        }

        /**
         * var R = 6371e3; // metres
         * var φ1 = lat1.toRadians();
         * var φ2 = lat2.toRadians();
         * var Δφ = (lat2-lat1).toRadians();
         * var Δλ = (lon2-lon1).toRadians();
         *
         * var a = Math.sin(Δφ/2) * Math.sin(Δφ/2) +
         *         Math.cos(φ1) * Math.cos(φ2) *
         *         Math.sin(Δλ/2) * Math.sin(Δλ/2);
         * var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
         *
         * var d = R * c;
         */

        double R = 6371e3;
        double lat1 = mozillaLocation.getLatRadians();
        double lat2 = gpsLocation.getLatRadians();
        Location delta = new Location(gpsLocation.getLat() - mozillaLocation.getLat(), gpsLocation.getLon() - mozillaLocation.getLon(), 0);
        double deltaLat = delta.getLatRadians();
        double deltaLon = delta.getLonRadians();

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d;
    }

    public boolean isValid() {
        return mozillaLocation != null && gpsLocation != null;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Location getMozillaLocation() {
        return mozillaLocation;
    }

    public Location getGpsLocation() {
        return gpsLocation;
    }

    public String getMozillaInput() {
        return mozillaInput;
    }
}
