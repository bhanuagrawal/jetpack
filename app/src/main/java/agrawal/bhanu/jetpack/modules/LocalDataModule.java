package agrawal.bhanu.jetpack.modules;


import android.app.Application;

import javax.inject.Singleton;

import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import dagger.Module;
import dagger.Provides;

@Module
public class LocalDataModule {


    @Provides
    @Singleton
    AppsRepository providesAppsRepository(Application application){
        AppsRepository appsRepository = new AppsRepository(application);
        return appsRepository;
    }
}
