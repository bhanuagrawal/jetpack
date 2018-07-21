package agrawal.bhanu.jetpack.launcher.data;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.ui.Apps;
import butterknife.internal.Utils;


public class AppsRepository {

    private final PackageManager packageManager;
    @Inject Gson gson;
    @Inject SharedPreferences sharedPreferences;
    Application application;

    public AppsRepository(Application application) {
        this.application = application;
        packageManager = application.getPackageManager();
        ((MyApp)application).getLocalDataComponent().inject(this);
    }

    public void fetchApps(MutableLiveData<AppsInfo> mCurrentApps) {

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> pkgAppsList = packageManager.queryIntentActivities( mainIntent, 0);
        ArrayList<AppDTO> apps = new ArrayList<>();
        ArrayList<AppDTO> appsUsageInfo = getAppUsageInfo();
        for(ResolveInfo app: pkgAppsList){
            AppDTO appDTO = new AppDTO();
            appDTO.setAppName(app.loadLabel(packageManager).toString());
            appDTO.setAppPackage(app.activityInfo.packageName);
            appDTO.setAppIcon(app.loadIcon(packageManager));
            AppDTO appUsage = getAppFromPackage(appsUsageInfo, app.activityInfo.packageName);
            if(appUsage != null){
                appDTO.setClicks(appUsage.getClicks());
                appDTO.setLastUsed(appUsage.getLastUsed());
                appDTO.setFolderIds(appUsage.getFolderIds());
            }

            if(appDTO.getFolderIds() == null){
                appDTO.setFolderIds(new ArrayList<String>());
            }
            addOrRemoveFromfrequentApps(appDTO);
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

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        appsInfo.setMessagingApp(getDefaultMessagingApp());

        Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        appsInfo.setInternetApp(getDefaultInternetApp(webIntent));
        appsInfo.setCallApp(getDefaultCallApp(new Intent(Intent.ACTION_DIAL)));
        appsInfo.setContactsApps(getDefaultApp(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)));
        appsInfo.setGoogleApp(getAppInfo(apps, "com.google.android.googlequicksearchbox"));
        appsInfo.setDefaultApps(new ArrayList<AppDTO>());
        if(appsInfo.getCallApp() != null){
            appsInfo.getDefaultApps().add(appsInfo.getCallApp());
        }
        if(appsInfo.getMessagingApp() != null){
            appsInfo.getDefaultApps().add(appsInfo.getMessagingApp());
        }
        if(appsInfo.getGoogleApp() != null){
            appsInfo.getDefaultApps().add(appsInfo.getGoogleApp());
        }
        if(appsInfo.getContactsApps() != null){
            appsInfo.getDefaultApps().add(appsInfo.getContactsApps());
        }
        if(appsInfo.getInternetApp() != null){
            appsInfo.getDefaultApps().add(appsInfo.getInternetApp());
        }

        mCurrentApps.postValue(appsInfo);

    }


    private ArrayList<AppDTO> getAppUsageInfo() {
        return gson.fromJson(sharedPreferences.getString("apps", "[]"), new TypeToken<ArrayList<AppDTO>>(){}.getType());
    }

    private AppDTO getDefaultCallApp(Intent intent) {
        List<ResolveInfo> callApps;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callApps = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        } else {
            callApps = packageManager.queryIntentActivities(intent, 0);
        }

        if(callApps != null && !callApps.isEmpty()){
            AppDTO appDTO = new AppDTO();
            appDTO.setAppName(callApps.get(0).loadLabel(packageManager).toString());
            appDTO.setAppPackage(callApps.get(0).activityInfo.packageName);
            appDTO.setAppIcon(callApps.get(0).loadIcon(packageManager));
            return appDTO;
        }

        return null;
    }


    private AppDTO getDefaultInternetApp(Intent intent) {
        List<ResolveInfo> browserList;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            browserList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        } else {
            browserList = packageManager.queryIntentActivities(intent, 0);
        }

        if(browserList != null && !browserList.isEmpty()){
            AppDTO appDTO = new AppDTO();
            appDTO.setAppName(browserList.get(0).loadLabel(packageManager).toString());
            appDTO.setAppPackage(browserList.get(0).activityInfo.packageName);
            appDTO.setAppIcon(browserList.get(0).loadIcon(packageManager));
            return appDTO;
        }

        return null;
    }

    private AppDTO getDefaultMessagingApp() {
        Intent intent;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String defaultApplication = Settings.Secure.getString(application.getContentResolver(),  "sms_default_application");
            PackageManager pm = application.getPackageManager();
            intent = pm.getLaunchIntentForPackage(defaultApplication );
        } else {
            intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setType("vnd.android-dir/mms-sms");
        }

        return getDefaultApp(intent);
    }


    public AppDTO getAppInfo(ArrayList<AppDTO> apps, String packageName){
        for(AppDTO appDTO: apps){
            if(appDTO.getAppPackage().equals(packageName)){
                return appDTO;
            }
        }

        return null;
    }

    private AppDTO getDefaultApp(Intent intent) {
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        if(resolveInfo != null){
            AppDTO appDTO = new AppDTO();
            appDTO.setAppName(resolveInfo.loadLabel(packageManager).toString());
            appDTO.setAppPackage(resolveInfo.activityInfo.packageName);
            appDTO.setAppIcon(resolveInfo.loadIcon(packageManager));
            return appDTO;
        }
        return null;

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

    public void saveAppsUsageInfo(ArrayList<AppDTO> apps) {

        ArrayList<AppDTO> appsInfo = new ArrayList<>();
        for(AppDTO app: apps){
            appsInfo.add(new AppDTO(app));
        }

        sharedPreferences.edit().putString("apps", gson.toJson(appsInfo)).commit();
    }

    public AppDTO getAppFromPackage(ArrayList<AppDTO> apps, String appPackage) {
        for(AppDTO appDTO: apps){
            if(appDTO.getAppPackage().equals(appPackage)){
                return appDTO;
            }
        }
        return null;
    }


    public void addOrRemoveFromfrequentApps(AppDTO appDTO) {

        if(appDTO.getLastUsed() != null){
            long diffHours = (new Date().getTime() - appDTO.getLastUsed().getTime())/ (60 * 60 * 1000);
            if(diffHours <= 24){
                if(appDTO.getFolderIds().indexOf(MainActivity.FREQUENT_APPS) < 0){
                    appDTO.getFolderIds().add(MainActivity.FREQUENT_APPS);
                }
            }
            else{
                if(appDTO.getFolderIds().indexOf(MainActivity.FREQUENT_APPS) >= 0){
                    appDTO.getFolderIds().remove(MainActivity.FREQUENT_APPS);
                }
            }
        }

    }

    public ArrayList<AppDTO> getAppsByFolderId(ArrayList<AppDTO> apps, String folderId) {

        ArrayList<AppDTO> suggestion = new ArrayList<>();
        for(AppDTO appDTO: apps){

            if(appDTO.getFolderIds().indexOf(folderId) >= 0){
                suggestion.add(appDTO);
            }

        }

        if(folderId.equals(MainActivity.FREQUENT_APPS)){
            Collections.sort(suggestion, new Comparator<AppDTO>() {
                @Override
                public int compare(AppDTO appDTO, AppDTO t1) {
                    return t1.getClicks()-appDTO.getClicks();
                }
            });
        }

        return suggestion;
    }
}
