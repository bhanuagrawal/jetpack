
package agrawal.bhanu.jetpack;
import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import agrawal.bhanu.jetpack.launcher.ui.AppList;
import agrawal.bhanu.jetpack.launcher.ui.Apps;
import agrawal.bhanu.jetpack.launcher.ui.AppsViewModel;
import agrawal.bhanu.jetpack.launcher.ui.DefaultPage;
import agrawal.bhanu.jetpack.launcher.ui.Home;
import agrawal.bhanu.jetpack.reddit.ui.ItemsList;

public class MainActivity extends AppCompatActivity
implements ItemsList.OnFragmentInteractionListener,
        AppList.OnFragmentInteractionListener,
        Apps.OnFragmentInteractionListener,
        Home.OnFragmentInteractionListener,
        DefaultPage.OnFragmentInteractionListener{

    private static final String BEER_DISPLAY_FRAGMENT = "sfdfdg";
    private static final String APP_LIST_FRAGMENT = "APPS_FRAGMENT";
    private static final String HOME_FRAGMENT = "homefragment";
    private BroadcastReceiver receiver;
    private Fragment homeFragment;
    private AppsViewModel mAppsModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            homeFragment = getSupportFragmentManager().getFragment(savedInstanceState, HOME_FRAGMENT);
        }
        else{
            homeFragment = Home.newInstance("", "");
        }

        mAppsModel = ViewModelProviders.of(this).get(AppsViewModel.class);
        int MyVersion = Build.VERSION.SDK_INT;
        if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!AppUtils.checkIfAlreadyhavePermission(getApplication())) {
                requestForSpecificPermission();
            }
            else {
                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.fragment_container, homeFragment, MainActivity.HOME_FRAGMENT).
                        commit();
            }
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addAction(Intent.ACTION_WALLPAPER_CHANGED);
        intentFilter.addAction(Intent.ACTION_SET_WALLPAPER);
        intentFilter.addDataScheme("package");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d("wallpapeer", "changes");
                if(intent.getAction() == Intent.ACTION_WALLPAPER_CHANGED){
                    ViewModelProviders.of(MainActivity.this).get(AppsViewModel.class).onWallpaperChange();
                }
                else{
                    ViewModelProviders.of(MainActivity.this).get(AppsViewModel.class).onAppListChange();
                }

            }
        };
        registerReceiver(receiver, intentFilter);
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(HOME_FRAGMENT);
        if(fragment != null &&
                fragment.isVisible()){
            ((Home)fragment).onBackPressed();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }



    @Override
    public void onFragmentCreated(Fragment fragment) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, HOME_FRAGMENT, homeFragment);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 101:

                getSupportFragmentManager().
                        beginTransaction().
                        replace(R.id.fragment_container, homeFragment, MainActivity.HOME_FRAGMENT).
                        commit();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
