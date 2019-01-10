package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabaseFactory;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.PreferencesService;

import java.util.List;

public class TestModule extends ServiceModule {

    private GpsLocationService gpsLocationService;
    private MozillaLocationRestClient mozillaLocationRestClient;
    private NetworkScanService networkScanService;
    private ReportRepository reportRepository;
    private AppDatabaseFactory appDatabaseFactory;
    private PreferencesService preferencesService;
    private List<Report> reports;

    public void setGpsLocationService(GpsLocationService gpsLocationService) {
        this.gpsLocationService = gpsLocationService;
    }

    public void setMozillaLocationRestClient(MozillaLocationRestClient mozillaLocationRestClient) {
        this.mozillaLocationRestClient = mozillaLocationRestClient;
    }

    public void setNetworkScanService(NetworkScanService networkScanService) {
        this.networkScanService = networkScanService;
    }

    public void setInitList(List<Report> reports) {
        this.reports = reports;
    }

    public void setAppDatabaseFactory(AppDatabaseFactory appDatabaseFactory) {
        this.appDatabaseFactory = appDatabaseFactory;
    }

    public void setPreferencesService(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @Override
    public GpsLocationService provideGpsLocationService() {
        return gpsLocationService != null ? gpsLocationService : super.provideGpsLocationService();
    }

    @Override
    public MozillaLocationRestClient provideMozillaLocationRestClient() {
        return mozillaLocationRestClient != null ? mozillaLocationRestClient : super.provideMozillaLocationRestClient();
    }

    @Override
    public NetworkScanService provideNetworkScanService() {
        return networkScanService != null ? networkScanService : super.provideNetworkScanService();
    }

    @Override
    public ReportRepository provideReportRepository(Application application, AppDatabaseFactory dbFactory) {
        if (reportRepository == null) {
            reportRepository = super.provideReportRepository(application, dbFactory);
            /*if (this.reports != null) {
                for (Report r: reports) {
                    reportRepository.insert(r);
                }
            }*/
        }
        return reportRepository;
    }

    @Override
    public AppDatabaseFactory provideAppDatabaseFactory() {
        return appDatabaseFactory != null ? appDatabaseFactory : super.provideAppDatabaseFactory();
    }

    @Override
    public PreferencesService providePreferencesService() {
        return preferencesService != null ? preferencesService : super.providePreferencesService();
    }
}
