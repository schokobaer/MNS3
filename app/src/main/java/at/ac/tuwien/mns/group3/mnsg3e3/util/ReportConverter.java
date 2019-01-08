package at.ac.tuwien.mns.group3.mnsg3e3.util;

import at.ac.tuwien.mns.group3.mnsg3e3.model.Location;
import at.ac.tuwien.mns.group3.mnsg3e3.model.LocationReport;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

public class ReportConverter {

    public static Report toModelView(LocationReport lr) {
        Report report = new Report(
                (int) (lr.getTimestamp().getTime() % Integer.MAX_VALUE),
                lr.getTimestamp().toLocaleString(),
                toLocationString(lr.getGpsLocation()),
                lr.getGpsLocation().getAccurency(),
                lr.getMozillaInput(),
                toLocationString(lr.getMozillaLocation()),
                lr.getDifference()
        );
        return report;
    }

    public static String toLocationString(Location location) {
        return location.getLat() + ", " + location.getLon();
    }
}
