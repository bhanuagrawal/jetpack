package agrawal.bhanu.jetpack.reddit.ui;

import android.app.Application;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.reddit.model.Post;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RedditPostViewModel extends ViewModel {

    PostDataSourceFactory postDataSourceFactory;
    private LiveData<PagedList<Post>> postList;
    private LiveData<NetworkState> networkState;
    private LiveData<NetworkState> initloading;




    @Inject
    public RedditPostViewModel(PostDataSourceFactory postDataSourceFactory) {
        super();
        this.postDataSourceFactory = postDataSourceFactory;
    }

    public LiveData<PagedList<Post>> getPostList() {

        if(postList == null){
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder()).setEnablePlaceholders(false)
                            .setInitialLoadSizeHint(20)
                            .setPageSize(20).build();

            postList = (new LivePagedListBuilder(postDataSourceFactory, pagedListConfig))
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
