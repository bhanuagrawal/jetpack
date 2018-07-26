package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Application;
import android.app.WallpaperManager;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.model.AppContainer;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsAndFolder;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.model.Folder;
import agrawal.bhanu.jetpack.launcher.util.callbacks.AddToHomeCallback;

public class LauncherViewModel extends AndroidViewModel {

    private MutableLiveData<AppsInfo> mCurrentApps;
    private MutableLiveData<ArrayList<AppsAndFolder>> folders;
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

    public MutableLiveData<ArrayList<AppsAndFolder>> getFolders() {
        if(folders == null){
            folders = new MutableLiveData<ArrayList<AppsAndFolder>>();
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
            mCurrentApps.setValue(new AppsInfo(0, 1, 0, new ArrayList<AppDTO>(), new ArrayList<AppDTO>()));
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
            mCurrentApps.setValue(new AppsInfo(0, 1, 0, new ArrayList<AppDTO>(), new ArrayList<AppDTO>()));
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

    public void onAppSelected(AppDTO app) {
        app = appsRepository.getAppFromPackage(getAppsInfo().getValue().getApps(), app.getAppPackage());
        app.setClicks(app.getClicks() + 1);
        app.setLastUsed(new Date());
        appsRepository.addOrRemoveFromfrequentApps(app);
        getAppsInfo().setValue(getAppsInfo().getValue());
        saveAppsUsageInfo();
    }

    public void saveAppsUsageInfo() {
        appsRepository.saveAppsUsageInfo(getAppsInfo().getValue().getApps());

    }

    public ArrayList<AppDTO> getAppsByFolderId(String folderId) {
        return appsRepository.getAppsByFolderId(getAppsInfo().getValue().getApps(), folderId);
    }

    public void onFoldersChange() {
        appsRepository.saveFoldersInfo(getFolders().getValue());
    }

    public Drawable getAppIcon(String appPackage) {
        return appsRepository.getAppIcon(appPackage);
    }

    public AppDTO getAppByContainer(AppContainer appContainer) {
        return appsRepository.getAppByContainerId(getAppsInfo().getValue().getApps(), appContainer);
    }

    public void addToFolder(AppDTO app, String folderId) {
        if(app.getFolderIds().indexOf(folderId) < 0){
            app.getFolderIds().add(folderId);
            getAppsInfo().setValue(getAppsInfo().getValue());
            appsRepository.saveAppsUsageInfo(getAppsInfo().getValue().getApps());
        }
    }

    public void addToHome(AppDTO app, int position, AddToHomeCallback addToHomeCallback){

        if(position>=0 && position<getFolders().getValue().size()){
            getFolders().getValue().set(position, new AppContainer(app.getAppPackage()));
            getFolders().setValue(getFolders().getValue());
            appsRepository.saveFoldersInfo(getFolders().getValue());
            addToFolder(app, app.getAppPackage());
            addToHomeCallback.onSuccess();
        }
        else {
            addToHomeCallback.onError("No space left on Home");
        }

    }

    public void addToHome(Folder folder, int position){
    }

    public int getEmptyPositionOnDefaultPage() {

        for(AppsAndFolder appsAndFolder: getFolders().getValue()){
            if(appsAndFolder instanceof AppContainer &&
                    appsRepository.getAppByContainerId(mCurrentApps.getValue().getApps(),
                            (AppContainer) appsAndFolder) == null){

                return getFolders().getValue().indexOf(appsAndFolder);

            }
            else if(appsAndFolder instanceof Folder &&
                    appsRepository.getAppsByFolderId(mCurrentApps.getValue().getApps(), ((Folder)appsAndFolder).getFolderId()).isEmpty()){
                return getFolders().getValue().indexOf(appsAndFolder);
            }
        }

        return -1;
    }

    public void removeFromHome(int position) {
        AppsAndFolder appsAndFolder = getFolders().getValue().get(position);
        getFolders().getValue().set(position, new AppContainer(""));
        getFolders().setValue(getFolders().getValue());
    }
}
