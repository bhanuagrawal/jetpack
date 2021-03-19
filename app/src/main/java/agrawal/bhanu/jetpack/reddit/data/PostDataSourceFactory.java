package agrawal.bhanu.jetpack.reddit.data;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.MyApp;

@Singleton
public class PostDataSourceFactory extends DataSource.Factory {

    ItemKeyedPostDataSource itemKeyedPostDataSource;
    Executor executor;
    MutableLiveData<ItemKeyedPostDataSource> mutableLiveData;

    @Inject
    public PostDataSourceFactory(Executor executor, ItemKeyedPostDataSource itemKeyedPostDataSource) {
        this.mutableLiveData = new MutableLiveData<ItemKeyedPostDataSource>();
        this.executor = executor;
        this.itemKeyedPostDataSource = itemKeyedPostDataSource;
    }

    public LiveData<ItemKeyedPostDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public androidx.paging.DataSource create() {
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }




}
