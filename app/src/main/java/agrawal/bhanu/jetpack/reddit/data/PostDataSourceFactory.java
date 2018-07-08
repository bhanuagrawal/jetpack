package agrawal.bhanu.jetpack.reddit.data;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;


public class PostDataSourceFactory implements android.arch.paging.DataSource.Factory {

    @Inject
    ItemKeyedPostDataSource itemKeyedPostDataSource;
    @Inject
    Executor executor;
    MutableLiveData<ItemKeyedPostDataSource> mutableLiveData;
    Application application;

    public PostDataSourceFactory(Application application) {
        this.application = application;
        this.mutableLiveData = new MutableLiveData<ItemKeyedPostDataSource>();
        ((MyApp)application).getWebComponent().inject(this);

    }

    public LiveData<ItemKeyedPostDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public android.arch.paging.DataSource create() {
        ((MyApp)application).getWebComponent().inject(this);
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }




}
