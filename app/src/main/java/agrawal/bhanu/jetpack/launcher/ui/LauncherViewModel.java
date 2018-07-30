package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Application;
import android.app.WallpaperManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.util.callbacks.AddToHomeCallback;

public class LauncherViewModel extends AndroidViewModel {

    private MutableLiveData<AppsInfo> mCurrentApps;
    private MutableLiveData<ArrayList<Widget>> folders;
    private MutableLiveData<Drawable> wallpaper;
    private Application application;
    @Inject
    AppsRepository appsRepository;


    @Inject
    Executor executor;

    @Inject
    WallpaperManager wallpaperManager;


    public LauncherViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        ((MyApp)application).getLocalDataComponent().inject(this);
    }

    public MutableLiveData<ArrayList<Widget>> getFolders() {
        if(folders == null){
            folders = new MutableLiveData<ArrayList<Widget>>();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    appsRepository.fetchFolders(folders);
                }
            });
        }
        return folders;
    }


    public MutableLiveData<AppsInfo> getAppsInfo() {

        if (mCurrentApps == null) {
            mCurrentApps = new MutableLiveData<AppsInfo>();
            mCurrentApps.setValue(new AppsInfo(new ArrayList<App>(), new ArrayList<App>()));
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    appsRepository.fetchApps(mCurrentApps);
                }
            });
        }
        return mCurrentApps;
    }

    public void onAppListChange() {

        if (mCurrentApps == null) {
            mCurrentApps = new MutableLiveData<AppsInfo>();
            mCurrentApps.setValue(new AppsInfo(new ArrayList<App>(), new ArrayList<App>()));
        }
        appsRepository.fetchApps(mCurrentApps);
    }

    public int getAppsCountPerPage() {
        return appsRepository.getAppRowCount()*appsRepository.getAppColumnCount();
    }

    public int getColumn_count() {
        return appsRepository.getAppColumnCount();
    }

    public void onWallpaperChange() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                AppUtils.checkIfAlreadyhavePermission(application)) {
            wallpaper.setValue(wallpaperManager.getDrawable());
        }
    }

    public MutableLiveData<Drawable> getWallpaper() {
        if(wallpaper == null){
            wallpaper = new MutableLiveData<>();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                    AppUtils.checkIfAlreadyhavePermission(application)) {
                wallpaper.setValue(wallpaperManager.getDrawable());
            }
        }
        return wallpaper;
    }

    public void onAppSelected(App app) {

        App afterChange = new App(app);
        afterChange.setClicks(app.getClicks() + 1);
        afterChange.setLastUsed(new Date());

        appsRepository.updateApp(app, afterChange);
        appsRepository.addOrRemoveFromfrequentApps(app);
    }

    public void saveAppsUsageInfo() {
        appsRepository.saveAppsUsageInfo(getAppsInfo().getValue().getApps());

    }

    public ArrayList<App> getAppsByFolderId(String folderId) {
        return appsRepository.getAppsByFolderId(folderId);
    }

    public void onFoldersChange() {
        ArrayList<Widget> widgets = getFolders().getValue();
        appsRepository.deleteAllWidgets();
        appsRepository.saveFoldersInfo(widgets);
    }

    public Drawable getAppIcon(String appPackage) {
        return appsRepository.getAppIcon(appPackage);
    }

    public App getAppByContainer(String appContainerId) {
        return appsRepository.getAppByContainerId(appContainerId);
    }

    public void addToFolder(App app, String folderId) {
        appsRepository.addToFolder(app, folderId, getAppsInfo());
    }

    public void addToHome(App app, AddToHomeCallback addToHomeCallback){


        int position = getEmptyPositionOnDefaultPage();
        if(position>=0 && position<getFolders().getValue().size()){

            AppContainer appContainer = new AppContainer(String.valueOf(new Timestamp(new Date().getTime())), app.getAppPackage());
            appsRepository.insertAppContainer(appContainer);
            Widget widget = new Widget(appContainer, position);
            appsRepository.updateWidgets(widget);
            addToHomeCallback.onSuccess();
        }
        else {
            addToHomeCallback.onError("No space left at Home");
        }

    }

    public void addToHome(Folder folder, int position){
    }

    public int getEmptyPositionOnDefaultPage() {

        for(Widget widget : getFolders().getValue()){

            if(widget.getType().equals(Constants.APP_CONTAINER) &&
                    appsRepository.getAppByContainerId(widget.getAppContainerId()) == null){

                return getFolders().getValue().indexOf(widget);

            }
            else if(widget.getType().equals(Constants.FOLDER)
                    && widget.getRemovable() &&
                    appsRepository.getAppsByFolderId(widget.getFolderId()).isEmpty()){
                return getFolders().getValue().indexOf(widget);
            }
        }

        return -1;
    }

    public void removeFromHome(int position) {
        appsRepository.removeFromHome(position, getAppsInfo());
    }

    public int getAppsPerPage() {
        return appsRepository.getAppColumnCount()*appsRepository.getAppRowCount();
    }

    public Folder getFolderById(String folderId) {
        return appsRepository.getFolderById(folderId);
    }
}
