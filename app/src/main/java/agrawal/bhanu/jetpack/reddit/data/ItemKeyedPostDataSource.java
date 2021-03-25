package agrawal.bhanu.jetpack.reddit.data;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.ItemKeyedDataSource;
import androidx.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.network.model.Status;
import agrawal.bhanu.jetpack.reddit.model.Post;
import agrawal.bhanu.jetpack.reddit.model.RedditFeed;

public class ItemKeyedPostDataSource extends ItemKeyedDataSource<String, Post> {

    PostRepository postRepository;
    Executor retryExecuter;
    private MutableLiveData networkState;
    private MutableLiveData initloading;
    private Runnable retryTask;

    @Override
    public void invalidate() {
        super.invalidate();
    }

    public MutableLiveData getInitloading() {
        return initloading;
    }

    @Inject
    public ItemKeyedPostDataSource( Executor retryExecuter, PostRepository postRepository) {
        this.retryExecuter = retryExecuter;
        this.postRepository = postRepository;
        networkState = new MutableLiveData();
        initloading = new MutableLiveData();
    }

    public MutableLiveData getNetworkState() {
        return networkState;
    }


    public void retry(){
        if(retryTask!=null){
            retryExecuter.execute(retryTask);
        }
    }

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<String> params, @NonNull final LoadInitialCallback<Post> callback) {


        networkState.postValue(NetworkState.LOADING);
        initloading.postValue(NetworkState.LOADING);
        postRepository.fetchPosts(String.valueOf(params.requestedLoadSize), null,"10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onResult(((RedditFeed)postRepository.parsePostResponse(response)).getMetaData().getChildren());
                        networkState.postValue(NetworkState.LOADED);
                        initloading.postValue(NetworkState.LOADED);
                    }},
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkState.postValue(new NetworkState(Status.FAILDED, error.getMessage()));
                        initloading.postValue(new NetworkState(Status.FAILDED, error.getMessage()));
                        retryTask = new Runnable() {
                            @Override
                            public void run() {
                                loadInitial(params, callback);
                            }
                        };

                    }
                }
        );
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<Post> callback) {

        networkState.postValue(NetworkState.LOADING);
        postRepository.fetchPosts(String.valueOf(params.requestedLoadSize), String.valueOf(params.key),"10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        callback.onResult(((RedditFeed)postRepository.parsePostResponse(response)).getMetaData().getChildren());
                        networkState.postValue(NetworkState.LOADED);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        networkState.postValue(new NetworkState(Status.FAILDED, error.getMessage()));
                        retryTask = new Runnable() {
                            @Override
                            public void run() {
                                loadAfter(params, callback);
                            }
                        };
                    }
                }
        );
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Post> callback) {

    }

    @NonNull
    @Override
    public String getKey(@NonNull Post item) {
        return item.getData().getName();
    }
}
