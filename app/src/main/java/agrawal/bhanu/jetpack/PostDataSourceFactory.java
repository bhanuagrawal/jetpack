package agrawal.bhanu.jetpack;
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.concurrent.Executor;

import javax.inject.Inject;


public class PostDataSourceFactory implements android.arch.paging.DataSource.Factory {

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
        itemKeyedPostDataSource = new ItemKeyedPostDataSource(application, executor);
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }




}
