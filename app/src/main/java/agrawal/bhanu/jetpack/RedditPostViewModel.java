package agrawal.bhanu.jetpack;

import android.app.Application;
import android.app.usage.NetworkStats;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.pojo.NetworkState;
import agrawal.bhanu.jetpack.pojo.reddit.Post;

public class RedditPostViewModel extends AndroidViewModel {

    private LiveData<PagedList<Post>> postList;
    private LiveData<NetworkState> networkState;
    LiveData<ItemKeyedPostDataSource> tDataSource;
    @Inject PostDataSourceFactory postDataSourceFactory;
    @Inject Executor executor;

    public LiveData<PagedList<Post>> getPostList() {

        if(postList == null){
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(30)
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

    public RedditPostViewModel(@NonNull Application application) {
        super(application);
        ((MyApp)application).getWebComponent().inject(this);
    }

}
