package at.ac.tuwien.mns.group3.mnsg3e3.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.UUID;

@Entity
public class Report {

    @PrimaryKey
    @NotNull
    private String date;
    private String cdn;
    private double precision;
    private String mls_param;
    private String mls_result;
    private double diff;

    public Report(String date, String cdn, double precision, String mls_param, String mls_result, double diff) {
        this.date = date;
        this.cdn = cdn;
        this.precision = precision;
        this.mls_param = mls_param;
        this.mls_result = mls_result;
        this.diff = diff;
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

    public String getMls_param() {
        return mls_param;
    }

    public String getMls_result() {
        return mls_result;
    }

    public double getDiff() {
        return diff;
    }
}
