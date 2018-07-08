
package agrawal.bhanu.jetpack;
import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import agrawal.bhanu.jetpack.launcher.ui.AppList;
import agrawal.bhanu.jetpack.launcher.ui.Apps;
import agrawal.bhanu.jetpack.launcher.ui.Home;
import agrawal.bhanu.jetpack.reddit.ui.ItemsList;

public class MainActivity extends AppCompatActivity
implements ItemsList.OnFragmentInteractionListener,
        AppList.OnFragmentInteractionListener,
        Apps.OnFragmentInteractionListener,
        Home.OnFragmentInteractionListener{

    private static final String BEER_DISPLAY_FRAGMENT = "sfdfdg";
    private static final String APP_LIST_FRAGMENT = "APPS_FRAGMENT";
    private static final String HOME_FRAGMENT = "homefragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!AppUtils.checkIfAlreadyhavePermission(getApplication())) {
                requestForSpecificPermission();
            }
            else {
                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.fragment_container, Home.newInstance("", ""), MainActivity.HOME_FRAGMENT).
                        commit();
            }
        }

/*        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, ItemsList.newInstance(0, ""), MainActivity.BEER_DISPLAY_FRAGMENT).
                commit();*/
/*
        getSupportFragmentManager().
                beginTransaction().
                replace(R.id.fragment_container, AppList.newInstance("", ""), MainActivity.APP_LIST_FRAGMENT).
                commit();*/
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:

                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.fragment_container, Home.newInstance("", ""), MainActivity.HOME_FRAGMENT).
                        commit();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
