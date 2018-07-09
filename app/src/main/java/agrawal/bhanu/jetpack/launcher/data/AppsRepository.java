package agrawal.bhanu.jetpack.launcher.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;


public class AppsRepository {

    private final PackageManager packageManager;
    Application application;

    public AppsRepository(Application application) {
        this.application = application;
        packageManager = application.getPackageManager();
    }

    public void fetchApps(MutableLiveData<AppsInfo> mCurrentApps) {

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities( mainIntent, 0);
        ArrayList<AppDTO> apps = new ArrayList<>();
        for(ResolveInfo app: pkgAppsList){
            AppDTO appDTO = new AppDTO();
            appDTO.setAppName(app.loadLabel(packageManager).toString());
            appDTO.setAppPackage(app.activityInfo.packageName);
            appDTO.setAppIcon(app.loadIcon(packageManager));
            apps.add(appDTO);
        }

        AppsInfo appsInfo = new AppsInfo();
        appsInfo.setApps(apps);
        Collections.sort(apps, new Comparator<AppDTO>() {
            @Override
            public int compare(AppDTO appDTO, AppDTO t1) {
                return appDTO.getAppName().compareTo(t1.getAppName());
            }
        });
        appsInfo.setRow_count(getAppRowCount());
        appsInfo.setColumn_count(getAppColumnCount());
        appsInfo.setApps_per_page(appsInfo.getColumn_count()*appsInfo.getRow_count());
        mCurrentApps.postValue(appsInfo);

    }


    public int getAppRowCount() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) application.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int height_px = Resources.getSystem().getDisplayMetrics().heightPixels;
        float pixeldpi = Resources.getSystem().getDisplayMetrics().density;
        int itemHeight_dp = 100;
        float itemHeight_px = pixeldpi * itemHeight_dp;

        return (int)(height_px/itemHeight_px) - 1;
    }

    public int getAppColumnCount() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) application.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width_px =Resources.getSystem().getDisplayMetrics().widthPixels;
        float pixeldpi = Resources.getSystem().getDisplayMetrics().density;
        int itemWidth_dp = 80;
        float itemWidth_px = pixeldpi * itemWidth_dp;
        //return  1;
        return (int)(width_px/itemWidth_px) -1;
    }

}
