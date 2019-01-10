package at.ac.tuwien.mns.group3.mnsg3e3.persistence;

import android.arch.persistence.room.Room;
import android.content.Context;
import com.commonsware.cwac.saferoom.SafeHelperFactory;

public class AppDatabaseFactory {

    private volatile AppDatabase INSTANCE;

    public AppDatabase getDatabase(final Context context, SafeHelperFactory factory) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,"report_database").openHelperFactory(factory).build();
                }
            }
        }
        return INSTANCE;
    }

    public AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "report_database").build();
                }
            }
        }
        return INSTANCE;
    }

    public void refreshInstance() {
        INSTANCE = null;
    }
}
