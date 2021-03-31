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
import dagger.hilt.EntryPoint;
import dagger.hilt.EntryPoints;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.components.SingletonComponent;

@ViewModelScoped
public class PostDataSourceFactory extends DataSource.Factory<String, Post> {

    Context context;
    MutableLiveData<ItemKeyedPostDataSource> mutableLiveData;

    @Inject
    public PostDataSourceFactory(@ApplicationContext Context context) {
        this.mutableLiveData = new MutableLiveData<ItemKeyedPostDataSource>();
        this.context = context;
    }

    public LiveData<ItemKeyedPostDataSource> getMutableLiveData() {
        return mutableLiveData;
    }

    @Override
    public androidx.paging.DataSource create() {
        ItemKeyedPostDataSource itemKeyedPostDataSource =
                EntryPoints
                        .get(context, PostDataSourceFactoryInterface.class)
                        .getItemKeyedPostDataSource();
        mutableLiveData.postValue(itemKeyedPostDataSource);
        return itemKeyedPostDataSource;
    }


    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface PostDataSourceFactoryInterface{
        ItemKeyedPostDataSource getItemKeyedPostDataSource();
    }


}
