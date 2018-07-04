package agrawal.bhanu.jetpack;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.pojo.RequestDetails;
import agrawal.bhanu.jetpack.pojo.reddit.Post;
import agrawal.bhanu.jetpack.pojo.reddit.Post;
import agrawal.bhanu.jetpack.pojo.reddit.RedditFeed;

public class PostRepository implements WebService.HtttpResponseListner {
    Application application;
    @Inject WebService webService;
    MutableLiveData<RedditFeed> redditFeed;
    @Inject Gson gson;
    private ItemKeyedDataSource.LoadInitialCallback<Post> initialCallback;
    private MutableLiveData networkState;
    @Inject
    Uri.Builder urlBuilder;
    private ItemKeyedDataSource.LoadCallback<Post> loadCallback;


    public PostRepository(Application application) {
        this.application = application;
        ((MyApp)application).getWebComponent().inject(this);
    }


    @Override
    public void onSuccess(RequestDetails requestDetails, Object object) {

        if(requestDetails.getRequestID() == Constants.REDDIT_FEED_REQUEST){
            RedditFeed feed = (RedditFeed) gson.fromJson((String) object, new TypeToken<RedditFeed>() {}.getType());
            Log.d("feedsize", String.valueOf(feed.getMetaData().getChildren().size()));
            initialCallback.onResult(feed.getMetaData().getChildren());
        }
        if(requestDetails.getRequestID() == Constants.REDDIT_FEED_REQUEST_PAGING){
            RedditFeed feed = (RedditFeed) gson.fromJson((String) object, new TypeToken<RedditFeed>() {}.getType());
            loadCallback.onResult(feed.getMetaData().getChildren());
        }

    }

    @Override
    public void onError(RequestDetails requestDetails, VolleyError error) {
        Log.d("PostRepository", error.toString());
    }

    public void fetchPosts(ItemKeyedDataSource.LoadInitialCallback<Post> callback, MutableLiveData networkState, String limit, String count) {
        this.initialCallback = callback;
        this.networkState = networkState;
        //networkState.postValue(NetworkState.LOADING);
        final RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestBody(null);
        requestDetails.setUrl(Constants.REDDIT_FEED_REQUEST_URL + "?limit=" + limit+"&count="+count);
        requestDetails.setRequestID(Constants.REDDIT_FEED_REQUEST);
        requestDetails.setRequestType(Request.Method.GET);
        webService.makeRequest(requestDetails, this);
    }

    public void fetchPosts(ItemKeyedDataSource.LoadParams<String> params, ItemKeyedDataSource.LoadCallback<Post> callback, MutableLiveData networkState, String limit, String after, String count) {
        this.loadCallback = callback;
        this.networkState = networkState;
        //networkState.postValue(NetworkState.LOADING);
        final RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestBody(null);
        requestDetails.setUrl(Constants.REDDIT_FEED_REQUEST_URL + "?limit=" + limit + "&after="+after+"&count="+count);
        requestDetails.setRequestID(Constants.REDDIT_FEED_REQUEST_PAGING);
        requestDetails.setRequestType(Request.Method.GET);
        webService.makeRequest(requestDetails, this);
    }
}
