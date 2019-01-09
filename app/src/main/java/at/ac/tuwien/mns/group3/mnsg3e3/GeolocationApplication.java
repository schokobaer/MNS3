package at.ac.tuwien.mns.group3.mnsg3e3;

import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppModule;
import at.ac.tuwien.mns.group3.mnsg3e3.di.DaggerAppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.ServiceModule;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasServiceInjector;

import javax.inject.Inject;

public class GeolocationApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .serviceModule(new ServiceModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
