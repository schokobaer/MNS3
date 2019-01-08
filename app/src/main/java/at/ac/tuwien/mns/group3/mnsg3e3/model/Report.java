package at.ac.tuwien.mns.group3.mnsg3e3.model;

public class Report {

    private int id;
    private String date;
    private String cdn;
    private double precision;
    private String mls_param;
    private String mls_result;
    private double diff;

    public Report(int id, String date, String cdn, double precision, String mls_param, String mls_result, double diff) {
        this.id = id;
        this.date = date;
        this.cdn = cdn;
        this.precision = precision;
        this.mls_param = mls_param;
        this.mls_result = mls_result;
        this.diff = diff;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCdn() {
        return cdn;
    }

    public double getPrecision() {
        return precision;
    }

    public String getMLSParam() {
        return mls_param;
    }

    public String getMLSResult() {
        return mls_result;
    }

    public double getDiff() {
        return diff;
    }
}
