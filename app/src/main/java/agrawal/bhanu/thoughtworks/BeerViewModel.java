package agrawal.bhanu.thoughtworks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import javax.inject.Inject;

import agrawal.bhanu.thoughtworks.pojo.BeerPOJO;

public class BeerViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<BeerPOJO>> beerList;
    @Inject BeerRepository beerRepository;

    public BeerViewModel(@NonNull Application application) {
        super(application);
        ((MyApp)application).getWebComponent().inject(this);
    }

    public MutableLiveData<ArrayList<BeerPOJO>> getBeersList() {

        if (beerList == null) {
            beerList = new MutableLiveData<ArrayList<BeerPOJO>>();
            beerRepository.fetchBeers(beerList);
        }
        return beerList;
    }
}
