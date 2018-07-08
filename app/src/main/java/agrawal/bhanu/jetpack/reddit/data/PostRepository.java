package agrawal.bhanu.jetpack.reddit.data;

import android.app.Application;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.network.model.RequestDetails;
import agrawal.bhanu.jetpack.network.WebService;
import agrawal.bhanu.jetpack.reddit.model.RedditFeed;

public class PostRepository implements WebService.HtttpResponseListner {
    Application application;
    @Inject WebService webService;
    @Inject Gson gson;
    @Inject
    Uri.Builder urlBuilder;


    public PostRepository(Application application) {
        this.application = application;
        ((MyApp)application).getWebComponent().inject(this);
    }

    public Object parseData(RequestDetails requestDetails, String response){
        if(requestDetails.getRequestID() == Constants.REDDIT_FEED_REQUEST){

        }

        return null;
    }



    @Override
    public void onResponse(RequestDetails requestDetails, Object object) {
    }

    @Override
    public void onError(RequestDetails requestDetails, VolleyError error) {
        Log.d("PostRepository", error.toString());
    }

    public void fetchPosts(String limit, String after, String count, Response.Listener onSuccess, Response.ErrorListener onError) {
        //networkState.postValue(NetworkState.LOADING);
        final RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestBody(null);

        if(after == null){
            requestDetails.setUrl(Constants.REDDIT_FEED_REQUEST_URL + "?limit=" + limit);
        }
        else {
            requestDetails.setUrl(Constants.REDDIT_FEED_REQUEST_URL + "?limit=" + limit + "&after="+after);
        }
        requestDetails.setRequestID(Constants.REDDIT_FEED_REQUEST);
        requestDetails.setRequestType(Request.Method.GET);
        requestDetails.setOnSuccess(onSuccess);
        requestDetails.setOnError(onError);
        webService.makeRequest(requestDetails, this);
    }

    public Object parsePostResponse(String response) {
        RedditFeed feed = (RedditFeed) gson.fromJson(response, new TypeToken<RedditFeed>() {}.getType());
        return feed;
    }
}
