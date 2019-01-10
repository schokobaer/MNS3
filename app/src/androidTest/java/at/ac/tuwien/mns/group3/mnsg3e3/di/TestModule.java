package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import at.ac.tuwien.mns.group3.mnsg3e3.model.Report;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.AppDatabase;
import at.ac.tuwien.mns.group3.mnsg3e3.persistence.ReportRepository;
import at.ac.tuwien.mns.group3.mnsg3e3.service.GpsLocationService;
import at.ac.tuwien.mns.group3.mnsg3e3.service.MozillaLocationRestClient;
import at.ac.tuwien.mns.group3.mnsg3e3.service.NetworkScanService;

import java.util.List;

public class TestModule extends ServiceModule {

    private GpsLocationService gpsLocationService;
    private MozillaLocationRestClient mozillaLocationRestClient;
    private NetworkScanService networkScanService;
    private ReportRepository reportRepository;
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
    public ReportRepository provideReportRepository(AppDatabase db) {
        if (reportRepository == null) {
            reportRepository = super.provideReportRepository(db);
            if (this.reports != null) {
                for (Report r: reports) {
                    reportRepository.insert(r);
                }
            }
        }
        return reportRepository;
    }

    @Override
    public AppDatabase provideAppDatabase(Application application) {
        return Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build();
    }
}
