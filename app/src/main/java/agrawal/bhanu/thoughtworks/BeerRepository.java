package agrawal.bhanu.thoughtworks;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import agrawal.bhanu.thoughtworks.pojo.BeerPOJO;

public class BeerRepository implements WebService.HtttpResonseListner{
    Application application;
    @Inject WebService webService;
    MutableLiveData<ArrayList<BeerPOJO>> beerList;
    @Inject Gson gson;


    public BeerRepository(Application application) {
        this.application = application;
        ((MyApp)application).getWebComponent().inject(this);
    }

    public void fetchBeers(MutableLiveData<ArrayList<BeerPOJO>> beerList) {
        this.beerList = beerList;
        RequestDetails requestDetails = new RequestDetails();
        requestDetails.setRequestBody(null);
        requestDetails.setUrl(Constants.BEER_LIST_REQUEST_URL);
        requestDetails.setRequestID(Constants.BEER_LIST_REQUEST);
        requestDetails.setRequestType(Request.Method.GET);
        webService.setHtttpResonseListner(this);
        webService.makeRequest(requestDetails);
    }


    @Override
    public void onSuccess(RequestDetails requestDetails, Object object) {

        if(requestDetails.getRequestID() == Constants.BEER_LIST_REQUEST){
            ArrayList<BeerPOJO> beers = (ArrayList<BeerPOJO>) gson.fromJson((String) object, new TypeToken<ArrayList<BeerPOJO>>() {
            }.getType());
            Collections.sort(beers, new Comparator<BeerPOJO>() {
                @Override
                public int compare(BeerPOJO beerPOJO, BeerPOJO t1) {
                    return beerPOJO.getName().compareTo(t1.getName());
                }
            });
            beerList.postValue(beers);
        }

    }

    @Override
    public void onError(RequestDetails requestDetails, VolleyError error) {

        Log.d("error", error.getMessage());
    }
}
