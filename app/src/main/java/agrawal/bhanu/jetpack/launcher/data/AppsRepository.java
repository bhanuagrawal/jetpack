package agrawal.bhanu.jetpack.launcher.data;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;


import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderApps;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderWidget;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback;


public class AppsRepository {

    private final PackageManager packageManager;
    @Inject Gson gson;
    @Inject SharedPreferences sharedPreferences;
    @Inject LauncherDatabase database;
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
        ArrayList<App> apps = new ArrayList<>();
        for(ResolveInfo app: pkgAppsList){
            App appDTO = new App();
            appDTO.setAppName(app.loadLabel(packageManager).toString());
            appDTO.setAppPackage(app.activityInfo.packageName);
            appDTO.setIcon(getAppIcon(app.activityInfo.packageName));
            App appUsage =  database.appsDao().getAppByPackagege(app.activityInfo.packageName);
            if(appUsage != null){
                appDTO.setClicks(appUsage.getClicks());
                appDTO.setLastUsed(appUsage.getLastUsed());
            }
            else {
                database.appsDao().insertAll(appDTO);
            }

            addOrRemoveFromfrequentApps(appDTO);
            apps.add(appDTO);
        }

        AppsInfo appsInfo = new AppsInfo();
        appsInfo.setApps(apps);
        Collections.sort(apps, new Comparator<App>() {
            @Override
            public int compare(App app, App t1) {
                return app.getAppName().compareTo(t1.getAppName());
            }
        });

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        appsInfo.setMessagingApp(getDefaultMessagingApp());

        Intent webIntent = new Intent("android.intent.action.VIEW", Uri.parse("http://"));
        appsInfo.setInternetApp(getDefaultInternetApp(webIntent));
        appsInfo.setCallApp(getDefaultCallApp(new Intent(Intent.ACTION_DIAL)));
        appsInfo.setContactsApps(getDefaultApp(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)));
        appsInfo.setGoogleApp(getAppInfo(apps, "com.google.android.googlequicksearchbox"));
        appsInfo.setDefaultApps(new ArrayList<App>());
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


        String[] appPackages = new String[apps.size()];
        int i=0;
        for(App app: apps){
            appPackages[i] = app.getAppPackage();
            i++;
        }

        database.appsDao().deleteUninstalled(appPackages);
        mCurrentApps.postValue(appsInfo);

    }


    private ArrayList<agrawal.bhanu.jetpack.launcher.data.entities.App> getAppUsageInfo() {
        return new ArrayList<>(Arrays.asList(database.appsDao().getAll()));
    }

    private App getDefaultCallApp(Intent intent) {
        List<ResolveInfo> callApps;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            callApps = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        } else {
            callApps = packageManager.queryIntentActivities(intent, 0);
        }

        if(callApps != null && !callApps.isEmpty()){
            App app = new App();
            app.setAppName(callApps.get(0).loadLabel(packageManager).toString());
            app.setAppPackage(callApps.get(0).activityInfo.packageName);
            app.setIcon(getAppIcon(callApps.get(0).activityInfo.packageName));
            return app;
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


    private App getDefaultInternetApp(Intent intent) {
        List<ResolveInfo> browserList;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            browserList = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        } else {
            browserList = packageManager.queryIntentActivities(intent, 0);
        }

        if(browserList != null && !browserList.isEmpty()){
            App app = new App();
            app.setAppName(browserList.get(0).loadLabel(packageManager).toString());
            app.setAppPackage(browserList.get(0).activityInfo.packageName);
            app.setIcon(getAppIcon(browserList.get(0).activityInfo.packageName));
            return app;
        }

        return null;
    }

    private App getDefaultMessagingApp() {
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


    public App getAppInfo(ArrayList<App> apps, String packageName){
        for(App app : apps){
            if(app.getAppPackage().equals(packageName)){
                return app;
            }
        }

        return null;
    }

    private App getDefaultApp(Intent intent) {
        ResolveInfo resolveInfo = packageManager.resolveActivity(intent,PackageManager.MATCH_DEFAULT_ONLY);
        if(resolveInfo != null){
            App app = new App();
            app.setAppName(resolveInfo.loadLabel(packageManager).toString());
            app.setAppPackage(resolveInfo.activityInfo.packageName);
            app.setIcon(getAppIcon(resolveInfo.activityInfo.packageName));
            return app;
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

        return (int)(height_px/itemHeight_px)-1;
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



    public void addOrRemoveFromfrequentApps(App app) {

        if(app.getLastUsed() != null){
            long diffHours = (new Date().getTime() - app.getLastUsed().getTime())/ (60 * 60 * 1000);
            if(diffHours <= 24){

                try {
                    database.folderAppsDao().insert(new FolderApps(MainActivity.FREQUENT_APPS, app.getAppPackage()));
                }
                catch (SQLiteConstraintException e){
                    Log.d("insert error", "already present");
                }
            }
            else{
                database.folderAppsDao().delete(app.getAppPackage());
            }
        }
    }

    public LiveData<List<App>> getAppsByFolderId(String folderId) {

        LiveData<List<App>> apps = database.folderAppsDao().getAppsByFolderId(folderId);
        return apps;
    }



    public void saveFoldersInfo(ArrayList<Widget> widgets, MutableLiveData<ArrayList<Widget>> widgetList) {
        for(Widget widget: widgets){
            Log.d("position", String.valueOf(widget.getPosition()));
        }
        database.widgetsDao().insertAll(widgets.toArray(new Widget[widgets.size()]));
        widgetList.postValue(widgets);
    }

    public LiveData<App> getAppByContainerId(String appContainerId) {
        return database.appContainerDao().getAppBYContainerId(appContainerId);
    }

    public void addToFolder(int position, String appId, Callback callback) {
        database.folderAppsDao().addToFolder(position, appId, callback);
    }

    public LiveData<FolderWidget> getFolderById(String folderId) {

        return database.folderDao().getFolderById(folderId);
    }

    public void removeFromHome(int position, MutableLiveData<ArrayList<Widget>> widgets) {
/*        database.widgetsDao().delete(position);
        Widget widget = new Widget(new Folder(UUID.randomUUID().toString(), "New Folder"), position);
        database.widgetsDao().insertAll(widget);

        widgets.postValue(getFolderFromMemory());*/
    }

    public void updateApp(App afterChange) {
        database.appsDao().update(afterChange);
    }

    public void insertAppContainer(AppContainer appContainer) {
        database.appContainerDao().insert(appContainer);
    }

    public void updateWidgets(MutableLiveData<ArrayList<Widget>> widgets, Widget widget, int position) {
/*        database.widgetsDao().delete(position);
        database.widgetsDao().insertAll(widget);
        widgets.postValue(getFolderFromMemory());*/
    }

    public void deleteAllWidgets() {
        database.widgetsDao().deleteAll();
    }


    public LiveData<List<WidgetsMetaData>> getWidgetLiveMetadata() {

        return  database.HomePageMetadataDao().getWidgetsLiveMetaData();
    }

    public void initializeHomePage() {
        int orientation = application.getResources().getConfiguration().orientation;
        int col_count = getAppColumnCountForHome(orientation);
        int row_count = getAppRowCountForHome(orientation)-2;
        for(int i=0; i<col_count*row_count; i++){

            if(i == 0){
                database.HomePageMetadataDao().createFolderWidget(i, Constants.FOLDER, false, MainActivity.FREQUENT_APPS, "Frequent Apps");
            }
            else{

                database.HomePageMetadataDao().createEmptyWidget(i, Constants.EMPTY);

            }

        }
    }

    public void insertAppAtPosition(App app, int position) {
        database.HomePageMetadataDao().insertAppAtPosition(app, position);
    }

    public void removeFromHome(int position) {
        database.HomePageMetadataDao().removeFromHome(position);
    }

    public void onWidgetPositionChange(List<WidgetsMetaData> value) {
        database.HomePageMetadataDao().onWidgetPositionChange(value);
    }

    public void addFolderAtPos(int position, Callback callback) {
        database.HomePageMetadataDao().addFolderAtPos(position, callback);
    }

    public String getJSonString(Class c, Object object) {

        String a = gson.toJson(object);
        return a;
    }

    public void removeFromHome(String folderId) {
        database.HomePageMetadataDao().removeFromHome(folderId);
    }
}
