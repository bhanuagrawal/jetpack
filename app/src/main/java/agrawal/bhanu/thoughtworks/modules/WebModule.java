package agrawal.bhanu.thoughtworks.modules;


import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.google.gson.Gson;

import javax.inject.Singleton;

import agrawal.bhanu.thoughtworks.BeerRepository;
import agrawal.bhanu.thoughtworks.WebService;
import dagger.Module;
import dagger.Provides;

@Module
public class WebModule {


    @Provides
    @Singleton
    public BeerRepository providesBeerRepository(Application application){
        return  new BeerRepository(application);
    }

    @Provides
    @Singleton
    public Gson providesGson(){
        return new Gson();
    }


    @Provides
    @Singleton
    public WebService providesWebService(Application application){
        return  new WebService(application);
    }
}
