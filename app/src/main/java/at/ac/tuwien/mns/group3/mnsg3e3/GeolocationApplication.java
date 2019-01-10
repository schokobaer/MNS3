package at.ac.tuwien.mns.group3.mnsg3e3;

import android.app.Application;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.AppModule;
import at.ac.tuwien.mns.group3.mnsg3e3.di.DaggerAppComponent;
import at.ac.tuwien.mns.group3.mnsg3e3.di.ServiceModule;

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

    public void setAppComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
