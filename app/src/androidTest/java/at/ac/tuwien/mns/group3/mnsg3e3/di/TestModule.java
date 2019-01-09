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
    private List<Report> initList;

    public TestModule(GpsLocationService gpsLocationService, MozillaLocationRestClient mozillaLocationRestClient, NetworkScanService networkScanService) {
        this.gpsLocationService = gpsLocationService;
        this.mozillaLocationRestClient = mozillaLocationRestClient;
        this.networkScanService = networkScanService;
    }

    public TestModule(GpsLocationService gpsLocationService, MozillaLocationRestClient mozillaLocationRestClient, NetworkScanService networkScanService, List<Report> initList) {
        this(gpsLocationService, mozillaLocationRestClient, networkScanService);
        this.initList = initList;
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
    public ReportRepository provideReportRepository(AppDatabase db) {
        ReportRepository repo = super.provideReportRepository(db);
        if (this.initList != null) {
            for (Report r: initList) {
                repo.insert(r);
            }
        }

        return repo;
    }

    @Override
    public AppDatabase provideAppDatabase(Application application) {
        return Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDatabase.class).build();
    }
}
