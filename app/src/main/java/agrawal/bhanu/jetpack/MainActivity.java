
package agrawal.bhanu.jetpack;
import android.Manifest;
import androidx.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import agrawal.bhanu.jetpack.launcher.ui.allapps.AppList;
import agrawal.bhanu.jetpack.launcher.ui.allapps.Apps;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolder;
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolderDialogFragmnet;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.DefaultPage;
import agrawal.bhanu.jetpack.launcher.ui.Home;
import agrawal.bhanu.jetpack.reddit.ui.ItemsList;

public class MainActivity extends AppCompatActivity
implements ItemsList.OnFragmentInteractionListener,
        AppList.OnFragmentInteractionListener,
        Apps.OnFragmentInteractionListener,
        Home.OnFragmentInteractionListener,
        DefaultPage.OnFragmentInteractionListener,
        AppsFolder.OnFragmentInteractionListener,
        AppsFolderDialogFragmnet.OnFragmentInteractionListener{

    private static final String BEER_DISPLAY_FRAGMENT = "sfdfdg";
    private static final String APP_LIST_FRAGMENT = "APPS_FRAGMENT";
    public static final String HOME_FRAGMENT = "homefragment";
    public static final String FREQUENT_APPS = "frequentFragment";
    public static final String APPS_DIALOG = "appsdialog";
    public static final String NEW_FOLDER = "asdfasdfasdf" ;
    private BroadcastReceiver receiver;
    private Fragment homeFragment;
    private LauncherViewModel mAppsModel;

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

        mAppsModel = ViewModelProviders.of(this).get(LauncherViewModel.class);
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
                    ViewModelProviders.of(MainActivity.this).get(LauncherViewModel.class).onWallpaperChange();
                }
                else{
                    ViewModelProviders.of(MainActivity.this).get(LauncherViewModel.class).onAppListChange();
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

        Fragment prev = getSupportFragmentManager().findFragmentByTag(MainActivity.APPS_DIALOG);
        if (prev != null  &&
                prev.isVisible()) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
            return;
        }

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
