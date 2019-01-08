package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

import java.util.List;

@Dao
public interface ReportDao {

    @Query("SELECT * FROM report")
    LiveData<List<Report>> getAll();

    @Insert
    void insertAll(Report... reports);

    @Delete
    void delete (Report report);
}
