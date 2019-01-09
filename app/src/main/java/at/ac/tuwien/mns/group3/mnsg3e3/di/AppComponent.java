package at.ac.tuwien.mns.group3.mnsg3e3.di;

import at.ac.tuwien.mns.group3.mnsg3e3.MainActivity;
import at.ac.tuwien.mns.group3.mnsg3e3.service.LocationReportIntentService;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = { AppModule.class, ServiceModule.class })
public interface AppComponent {

    void inject(LocationReportIntentService intentService);
    void inject(MainActivity activity);
}
