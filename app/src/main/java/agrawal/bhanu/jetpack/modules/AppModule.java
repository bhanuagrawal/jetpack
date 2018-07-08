package agrawal.bhanu.jetpack.modules;

import android.app.Application;
import android.app.WallpaperManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application){
        mApplication = application;
    }

    @Provides
    @Singleton
    public Application providesApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    public RequestQueue providesRequestQueue(Application application){
        return Volley.newRequestQueue(application.getApplicationContext());

    }


    @Provides
    @Singleton
    public WallpaperManager getWallpaperManager(Application application){
        return WallpaperManager.getInstance(application.getApplicationContext());
    }

    @Provides
    @Singleton
    public Executor providesExecuter(){
        return  Executors.newFixedThreadPool(5);
    }
}
