package agrawal.bhanu.thoughtworks.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import agrawal.bhanu.thoughtworks.fragments.BeerList;
import agrawal.bhanu.thoughtworks.fragments.BeersDisplayFragment;
import agrawal.bhanu.thoughtworks.R;

public class MainActivity extends AppCompatActivity implements BeersDisplayFragment.OnFragmentInteractionListener,
        BeerList.OnFragmentInteractionListener {

    private static final String BEER_DISPLAY_FRAGMENT = "sfdfdg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, BeersDisplayFragment.newInstance("", ""), MainActivity.BEER_DISPLAY_FRAGMENT).
                commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
