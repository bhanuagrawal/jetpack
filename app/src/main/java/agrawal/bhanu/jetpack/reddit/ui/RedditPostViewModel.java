package agrawal.bhanu.jetpack.reddit.ui;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.reddit.model.Post;

public class RedditPostViewModel extends AndroidViewModel {

    private LiveData<PagedList<Post>> postList;
    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> initloading;


    @Inject
    PostDataSourceFactory postDataSourceFactory;
    @Inject Executor executor;

    public RedditPostViewModel(@NonNull Application application) {
        super(application);
        ((MyApp)application).getWebComponent().inject(this);
    }

    public LiveData<PagedList<Post>> getPostList() {

        if(postList == null){
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(20)
                            .setPageSize(20).build();

            postList = (new LivePagedListBuilder(postDataSourceFactory, pagedListConfig))
                    .setBackgroundThreadExecutor(executor)
                    .build();
        }
        return postList;
    }

    public LiveData<NetworkState> getNetworkState() {

        networkState = Transformations.switchMap(postDataSourceFactory.getMutableLiveData(), new Function<ItemKeyedPostDataSource, LiveData<NetworkState>>() {
            @Override
            public LiveData<NetworkState> apply(ItemKeyedPostDataSource input) {
                return input.getNetworkState();
            }
        });

        return networkState;
    }

    public LiveData<NetworkState> getInitloading() {
       initloading = Transformations.switchMap(postDataSourceFactory.getMutableLiveData(), new Function<ItemKeyedPostDataSource, LiveData<NetworkState>>() {
           @Override
           public LiveData<NetworkState> apply(ItemKeyedPostDataSource input) {
               return input.getInitloading();
           }
       });
       return initloading;
    }


    public void onRefresh() {
        postDataSourceFactory.getMutableLiveData().getValue().invalidate();
    }

    public void retry(){
        postDataSourceFactory.getMutableLiveData().getValue().retry();
    }
}
