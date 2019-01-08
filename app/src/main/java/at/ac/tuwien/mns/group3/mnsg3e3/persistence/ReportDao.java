package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

import java.util.List;

public interface ReportDao {

    @Query("SELECT * FROM report")
    List<Report> getAll();

    @Insert
    void insertAll(Report... reports);

    @Delete
    void delete (Report report);
}
