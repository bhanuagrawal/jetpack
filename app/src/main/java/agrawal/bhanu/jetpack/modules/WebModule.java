package agrawal.bhanu.jetpack.modules;


import android.app.Application;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;
import javax.inject.Scope;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.PostDataSourceFactory;
import agrawal.bhanu.jetpack.PostRepository;
import agrawal.bhanu.jetpack.WebService;
import agrawal.bhanu.jetpack.adapters.ItemsAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class WebModule {

    @Provides
    @Singleton
    public PostRepository providesBeerRepository(Application application){
        return  new PostRepository(application);
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

    @Provides
    @Singleton
    public Executor providesExecuter(){
        return  Executors.newFixedThreadPool(5);
    }

    @Provides
    @Singleton
    public PostDataSourceFactory providePostDataSourceFactory(Application application){
        return new PostDataSourceFactory(application);
    }

    @Provides
    @Singleton
    public ItemsAdapter providesItemAdapter(Application application){
        return new ItemsAdapter(application);
    }

    @Provides
    @Singleton
    public Uri.Builder provideUrlBuilder(){
        return new Uri.Builder();
    }
}
