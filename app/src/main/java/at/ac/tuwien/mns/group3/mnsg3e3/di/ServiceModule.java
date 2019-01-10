package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.util.Base64;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabaseFactory;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.PreferencesService;
import com.commonsware.cwac.saferoom.SafeHelperFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class ServiceModule {

    static {
        System.loadLibrary("keys");
    }

    public native String getMlsApiKey();

    @Singleton
    @Provides
    public GpsLocationService provideGpsLocationService() {
        return new GpsLocationService();
    }

    @Singleton
    @Provides
    public MozillaLocationRestClient provideMozillaLocationRestClient() {
        MozillaLocationRestClient s = new MozillaLocationRestClient();
        s.setApiKey(new String(Base64.decode(getMlsApiKey(), Base64.DEFAULT)));
        return s;
    }

    @Singleton
    @Provides
    public NetworkScanService provideNetworkScanService() {
        return new NetworkScanService();
    }

    @Singleton
    @Provides
    public ReportRepository provideReportRepository(Application application, AppDatabaseFactory dbFactory) {
        return new ReportRepository(application, dbFactory);
    }

    @Singleton
    @Provides
    public AppDatabaseFactory provideAppDatabaseFactory() {
        return new AppDatabaseFactory();
    }

    @Singleton
    @Provides
    public PreferencesService providePreferencesService() {
        return new PreferencesService();
    }
}
