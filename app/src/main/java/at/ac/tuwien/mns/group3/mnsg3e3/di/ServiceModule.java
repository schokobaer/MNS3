package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ServiceModule {

    @Singleton
    @Provides
    public GpsLocationService provideGpsLocationService() {
        return new GpsLocationService();
    }

    @Singleton
    @Provides
    public MozillaLocationRestClient provideMozillaLocationRestClient() {
        MozillaLocationRestClient s = new MozillaLocationRestClient();
        s.setApiKey("test");
        return s;
    }

    @Singleton
    @Provides
    public NetworkScanService provideNetworkScanService() {
        return new NetworkScanService();
    }

    @Singleton
    @Provides
    public ReportRepository provideReportRepository(Application application) {
        return new ReportRepository(application);
    }

    /*
    @Singleton
    @Provides
    public AppDatabase provideAppDatabase(Application application) {
        return Room.databaseBuilder(application.getApplicationContext(), AppDatabase.class, "report_database").build();
    }*/
}
