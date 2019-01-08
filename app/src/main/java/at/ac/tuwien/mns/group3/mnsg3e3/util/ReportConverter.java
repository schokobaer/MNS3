package at.ac.tuwien.mns.group3.mnsg3e3.util;

import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

import java.text.SimpleDateFormat;

public class ReportConverter {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public static Report toModelView(LocationReport lr) {
        Report report = new Report(
                simpleDateFormat.format(lr.getTimestamp()),
                toLocationString(lr.getGpsLocation()),
                lr.getGpsLocation() != null ? lr.getGpsLocation().getAccurency() : 0,
                lr.getMozillaInput(),
                toLocationString(lr.getMozillaLocation()),
                lr.getDifference()
        );
        return report;
    }

    public static String toLocationString(Location location) {
        if (location == null) {
            return "N/A";
        }
        return location.getLat() + ", " + location.getLon();
    }
}
