package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;

public class TestModule extends ServiceModule {

    private GpsLocationService gpsLocationService;
    private MozillaLocationRestClient mozillaLocationRestClient;
    private NetworkScanService networkScanService;
    private ReportRepository repo;

    public TestModule(GpsLocationService gpsLocationService, MozillaLocationRestClient mozillaLocationRestClient, NetworkScanService networkScanService, ReportRepository repo) {
        this.gpsLocationService = gpsLocationService;
        this.mozillaLocationRestClient = mozillaLocationRestClient;
        this.networkScanService = networkScanService;
        this.repo = repo;
    }

    @Override
    public GpsLocationService provideGpsLocationService() {
        return gpsLocationService;
    }

    @Override
    public MozillaLocationRestClient provideMozillaLocationRestClient() {
        return mozillaLocationRestClient;
    }

    @Override
    public NetworkScanService provideNetworkScanService() {
        return networkScanService;
    }

    @Override
    public ReportRepository provideReportRepository(Application application) {
        if (repo != null) {
            return repo;
        }
        return super.provideReportRepository(application);
    }
}
