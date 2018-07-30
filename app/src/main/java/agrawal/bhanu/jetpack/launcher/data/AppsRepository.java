package agrawal.bhanu.jetpack.launcher.data;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.WindowManager;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.model.AppContainer;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsAndFolder;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.model.Folder;
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolder;


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
            appDTO.setIcon(getAppIcon(app.activityInfo.packageName));
            AppDTO appUsage = getAppFromPackage(appsUsageInfo, app.activityInfo.packageName);
            if(appUsage != null){
                appDTO.setClicks(appUsage.getClicks());
                appDTO.setLastUsed(appUsage.getLastUsed());
                appDTO.setFolderIds(appUsage.getFolderIds());
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
            appDTO.setIcon(getAppIcon(callApps.get(0).activityInfo.packageName));
            return appDTO;
        }

        return null;
    }


    public Drawable getAppIcon(String packageName){
        try {
            return packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
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
            appDTO.setIcon(getAppIcon(browserList.get(0).activityInfo.packageName));
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
            appDTO.setIcon(getAppIcon(resolveInfo.activityInfo.packageName));
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
        int itemHeight_dp = 95;
        float itemHeight_px = pixeldpi * itemHeight_dp;

        return (int)(height_px/itemHeight_px);
    }

    public int getAppColumnCount() {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) application.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width_px =Resources.getSystem().getDisplayMetrics().widthPixels;
        float pixeldpi = Resources.getSystem().getDisplayMetrics().density;
        int itemWidth_dp = 85;
        float itemWidth_px = pixeldpi * itemWidth_dp;
        //return  1;
        return (int)(width_px/itemWidth_px);
    }

    public int getAppRowCountForHome(int orientation) {


        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) application.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int height_px;
        float pixeldpi = Resources.getSystem().getDisplayMetrics().density;
        int itemHeight_dp = 95;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            height_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        else{
            height_px = Resources.getSystem().getDisplayMetrics().heightPixels;
        }

        float itemHeight_px = pixeldpi * itemHeight_dp;

        return (int)(height_px/itemHeight_px);
    }

    public int getAppColumnCountForHome(int orientation) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) application.getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int width_px;
        float pixeldpi = Resources.getSystem().getDisplayMetrics().density;
        int itemWidth_dp = 85;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            width_px = Resources.getSystem().getDisplayMetrics().heightPixels;
        }
        else{
            width_px = Resources.getSystem().getDisplayMetrics().widthPixels;
        }
        float itemWidth_px = pixeldpi * itemWidth_dp;
        //return  1;
        return (int)(width_px/itemWidth_px);
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

    public void fetchFolders(MutableLiveData<ArrayList<AppsAndFolder>> folders) {

        ArrayList<AppsAndFolder> prevAppsAndFolders = getFolderFromMemory();

        if(prevAppsAndFolders.isEmpty()){
            Folder frequestApps = new Folder("Frequent Apps", MainActivity.FREQUENT_APPS);
            frequestApps.setRemovable(false);

            ArrayList<AppsAndFolder> foldersList = new ArrayList<>();

            int orientation = application.getResources().getConfiguration().orientation;
            int col_count = getAppColumnCountForHome(orientation);
            int row_count = getAppRowCountForHome(orientation)-2;
            for(int i=0; i<col_count*row_count-1; i++){
                Folder f = new Folder("", "");
                foldersList.add(f);
            }

            foldersList.add(frequestApps);
            Collections.reverse(foldersList);
            saveFoldersInfo(foldersList);
            folders.postValue(foldersList);
        }
        else {
            folders.postValue(prevAppsAndFolders);
        }



    }

    private ArrayList<AppsAndFolder> getFolderFromMemory() {
//        return new ArrayList<>();

        RuntimeTypeAdapterFactory<AppsAndFolder> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(AppsAndFolder.class, "type")
                .registerSubtype(AppContainer.class, AppsAndFolder.APP)
                .registerSubtype(Folder.class, AppsAndFolder.FOLDER);

        Gson gsonRuntimeTypeAdapterFactory = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        ArrayList<AppsAndFolder> appsFolders =  gsonRuntimeTypeAdapterFactory.fromJson(sharedPreferences.getString("folders", "[]"), new TypeToken<ArrayList<AppsAndFolder>>(){}.getType());
        for(AppsAndFolder appsAndFolder: appsFolders){
            if(appsAndFolder instanceof AppContainer){
                appsAndFolder.setType(AppsAndFolder.APP);
            }
            else if(appsAndFolder instanceof Folder){
                appsAndFolder.setType(AppsAndFolder.FOLDER);
            }
        }
        return appsFolders;
    }

    public void saveFoldersInfo(ArrayList<AppsAndFolder> appsFolders) {

        RuntimeTypeAdapterFactory<AppsAndFolder> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
                .of(AppsAndFolder.class, "type")
                .registerSubtype(AppContainer.class, AppsAndFolder.APP)
                .registerSubtype(Folder.class, AppsAndFolder.FOLDER);
        Gson gsonRuntimeTypeAdapterFactory = new GsonBuilder().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
        sharedPreferences.edit().putString("folders", gsonRuntimeTypeAdapterFactory.toJson(appsFolders)).commit();
    }

    public AppDTO getAppByContainerId(ArrayList<AppDTO> apps, AppContainer appContainer) {

        for(AppDTO appDTO: apps){
            if(appDTO.getFolderIds().indexOf(appContainer.getContainerId()) >= 0){
                return appDTO;
            }
        }
        return null;
    }
}
