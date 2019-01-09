package at.ac.tuwien.mns.group3.mnsg3e3.di;

import android.app.Application;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {

    Application app;

    public AppModule(Application app) {
        this.app = app;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return app;
    }
}
