package agrawal.bhanu.thoughtworks;

import java.util.ArrayList;

import agrawal.bhanu.thoughtworks.pojo.BeerPOJO;

public class AppUtils {
    public static int getNoOfPages(ArrayList<BeerPOJO> beers) {
        return (int) Math.ceil((float)beers.size()/100);
    }

    public static ArrayList<BeerPOJO> getBeers(ArrayList<BeerPOJO> beers, int pageNo) {
        return new ArrayList<BeerPOJO>(beers.subList(pageNo*100, beers.size()>pageNo*100 + 100?  pageNo*100 + 100: beers.size()));

    }
}
