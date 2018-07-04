package agrawal.bhanu.jetpack.activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import agrawal.bhanu.jetpack.fragments.ItemsList;
import agrawal.bhanu.jetpack.R;

public class MainActivity extends AppCompatActivity
       implements ItemsList.OnFragmentInteractionListener {

    private static final String BEER_DISPLAY_FRAGMENT = "sfdfdg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, ItemsList.newInstance(0, ""), MainActivity.BEER_DISPLAY_FRAGMENT).
                commit();

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
