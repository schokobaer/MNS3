package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;

@Database(entities={Report.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ReportDao reportDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "report_database").build();
                }
            }
        }
        return INSTANCE;
    }
}
