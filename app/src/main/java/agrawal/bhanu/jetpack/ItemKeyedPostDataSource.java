package agrawal.bhanu.jetpack;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.pojo.NetworkState;
import agrawal.bhanu.jetpack.pojo.reddit.Post;
import agrawal.bhanu.jetpack.pojo.reddit.Post;

public class ItemKeyedPostDataSource extends ItemKeyedDataSource<String, Post> {

    @Inject PostRepository postRepository;
    private MutableLiveData networkState;


    public ItemKeyedPostDataSource(Application application) {
        networkState = new MutableLiveData();
        ((MyApp)application).getWebComponent().inject(this);
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Post> callback) {

        postRepository.fetchPosts(callback, networkState, String.valueOf(params.requestedLoadSize), "10");
    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {
        postRepository.fetchPosts(params, callback, networkState, String.valueOf(params.requestedLoadSize), String.valueOf(params.key), "10");
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Post item) {
        return item.getData().getId();
    }
}
