package agrawal.bhanu.jetpack.reddit.data;
import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;


public class PostDataSourceFactory extends DataSource.Factory {

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
    public androidx.paging.DataSource create() {
        ((MyApp)application).getWebComponent().inject(this);
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }




}
