package agrawal.bhanu.thoughtworks.modules;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

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
}
