package agrawal.bhanu.jetpack.reddit.data;
import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.reddit.model.Post;

@Singleton
public class PostDataSourceFactory extends DataSource.Factory<String, Post> {
    Provider<ItemKeyedPostDataSource> provider;
    MutableLiveData<ItemKeyedPostDataSource> mutableLiveData;

    @Inject
    public PostDataSourceFactory(Provider<ItemKeyedPostDataSource> provider) {
        this.mutableLiveData = new MutableLiveData<ItemKeyedPostDataSource>();
        this.provider = provider;
    }

    public LiveData<ItemKeyedPostDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public androidx.paging.DataSource create() {
        ItemKeyedPostDataSource itemKeyedPostDataSource = provider.get();
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }




}
