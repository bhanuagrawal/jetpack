package agrawal.bhanu.jetpack;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.pojo.BeerPOJO;
import agrawal.bhanu.jetpack.pojo.reddit.RedditFeed;

public class AppUtils {
    public static int getNoOfPages(RedditFeed redditFeed) {
        return (int) Math.ceil((float)redditFeed.getMetaData().getChildren().size()/100);
    }

    public static ArrayList<BeerPOJO> getBeers(ArrayList<BeerPOJO> beers, int pageNo) {
        return new ArrayList<BeerPOJO>(beers.subList(pageNo*100, beers.size()>pageNo*100 + 100?  pageNo*100 + 100: beers.size()));

    }
}
