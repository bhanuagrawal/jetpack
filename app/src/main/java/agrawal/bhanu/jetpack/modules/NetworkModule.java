package agrawal.bhanu.jetpack.modules;


import android.app.Application;
import android.net.Uri;

import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory;
import agrawal.bhanu.jetpack.reddit.data.PostRepository;
import agrawal.bhanu.jetpack.network.WebService;
import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    public PostRepository providesPostRepository(Application application){
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
    public PostDataSourceFactory providePostDataSourceFactory(Application application){
        return new PostDataSourceFactory(application);
    }

    @Provides
    @Singleton
    public ItemsAdapter providesItemAdapter(Application application){
        return new ItemsAdapter(application);
    }

    @Provides
    public ItemKeyedPostDataSource providesItemKeyedPostDataSource(Application application, Executor retryExecuter){
        return new ItemKeyedPostDataSource(application, retryExecuter);
    }

    @Provides
    @Singleton
    public Uri.Builder provideUrlBuilder(){
        return new Uri.Builder();
    }
}
