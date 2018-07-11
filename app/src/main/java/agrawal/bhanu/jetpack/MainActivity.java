
package agrawal.bhanu.jetpack;
import android.Manifest;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
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
    private BroadcastReceiver appListChangeReceiver;

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

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        intentFilter.addDataScheme("package");
        appListChangeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ViewModelProviders.of(MainActivity.this).get(AppsViewModel.class).onAppListChange();
            }
        };
        registerReceiver(appListChangeReceiver, intentFilter);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(appListChangeReceiver);
    }
}
