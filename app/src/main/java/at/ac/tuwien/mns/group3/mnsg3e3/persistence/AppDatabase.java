package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.persistence.room.RoomDatabase;

public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();
}
