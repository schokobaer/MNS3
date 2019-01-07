package at.ac.tuwien.mns.group3.mnsg3e3.model;

public class Report {

    private int id;
    private String date;
    private String cdn;
    private float precision;
    private String mls_param;
    private String mls_result;
    private float diff;

    public Report(int id, String date, String cdn, float precision, String mls_param, String mls_result, float diff) {
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

    public float getPrecision() {
        return precision;
    }

    public String getMLSParam() {
        return mls_param;
    }

    public String getMLSResult() {
        return mls_result;
    }

    public float getDiff() {
        return diff;
    }
}
