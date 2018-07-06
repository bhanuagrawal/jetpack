package agrawal.bhanu.jetpack;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.model.NetworkState;
import agrawal.bhanu.jetpack.model.reddit.Post;
import agrawal.bhanu.jetpack.model.reddit.RedditFeed;

public class ItemKeyedPostDataSource extends ItemKeyedDataSource<String, Post> {

    @Inject
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

    public ItemKeyedPostDataSource(Application application, Executor retryExecuter) {
        this.retryExecuter = retryExecuter;
        networkState = new MutableLiveData();
        initloading = new MutableLiveData();
        ((MyApp)application).getWebComponent().inject(this);
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
