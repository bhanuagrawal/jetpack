package agrawal.bhanu.jetpack.modules;


import android.app.Application;
import androidx.room.Room;

import javax.inject.Singleton;

import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.data.LauncherDatabase;
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


    @Provides
    @Singleton
    LauncherDatabase providesLauncherDatabase(Application application){
        LauncherDatabase db = Room.databaseBuilder(application,
                LauncherDatabase.class, "launcher-data").build();
        return db;
    }
}
