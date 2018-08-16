package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Application;
import android.app.WallpaperManager;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderWidget;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback;

public class LauncherViewModel extends AndroidViewModel {

    private MutableLiveData<AppsInfo> mCurrentApps;
    private MutableLiveData<Drawable> wallpaper;
    private LiveData<List<WidgetsMetaData>> widgetMetaData;
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

    public LiveData<List<WidgetsMetaData>> getWidgetsLiveMetadata() {
        if(widgetMetaData == null){
            widgetMetaData =  appsRepository.getWidgetLiveMetadata();
        }
        return widgetMetaData;
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
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.fetchApps(mCurrentApps);
            }
        });
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

    public void onAppSelected(final App app) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                App afterChange = new App(app);
                afterChange.setClicks(app.getClicks() + 1);
                afterChange.setLastUsed(new Date());
                appsRepository.updateApp(afterChange);
                appsRepository.addOrRemoveFromfrequentApps(afterChange);
            }
        });

    }


    public LiveData<List<App>> getAppsByFolderId(final String folderId) {
        return Transformations.switchMap(mCurrentApps, new Function<AppsInfo, LiveData<List<App>>>() {
            @Override
            public LiveData<List<App>> apply(AppsInfo input) {
                return appsRepository.getAppsByFolderId(folderId);
            }
        });
    }

    public void onFoldersChange() {
/*        ArrayList<Widget> widgets = getWidgetsLiveMetadata().getValue();
        appsRepository.deleteAllWidgets();
        appsRepository.saveFoldersInfo(widgets, getWidgetsLiveMetadata());*/
    }

    public Drawable getAppIcon(String appPackage) {
        return appsRepository.getAppIcon(appPackage);
    }

    public LiveData<App> getAppByContainer(String appContainerId) {
        return appsRepository.getAppByContainerId(appContainerId);
    }

    public void addToFolder(final int position, final String appId, final Callback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.addToFolder(position, appId, callback);
            }
        });
    }

    public void addToHome(final App app, final Callback callback){

        executor.execute(new Runnable() {
            @Override
            public void run() {
                int position = getEmptyPositionOnHome();
                if(position >=0){
                    appsRepository.insertAppAtPosition(app, position);
                    callback.onSuccess();
                }
                else {
                    callback.onError("No space left at Home");
                }
            }
        });

    }

    private int getEmptyPositionOnHome() {
        List<WidgetsMetaData> widgetsMetaData = getWidgetsLiveMetadata().getValue();
        for(WidgetsMetaData widget: widgetsMetaData){
            if(widget.getType().equals(Constants.EMPTY) ||
                    (widget.getType().equals(Constants.APP_CONTAINER) && widget.getAppId() == null) ||
                    (widget.getType().equals(Constants.FOLDER) && widget.getAppsCount() == 0 && widget.isRemovable()) ){
                return widget.getPosition();
            }
        }

        return -1;
    }

    public void addToHome(Folder folder, int position){
    }


    public void removeFromHome(final int position) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.removeFromHome(position);
            }
        });
    }

    public int getAppsPerPage() {
        return appsRepository.getAppColumnCount()*appsRepository.getAppRowCount();
    }

    public LiveData<FolderWidget> getFolderById(String folderId) {
        return appsRepository.getFolderById(folderId);
    }

    public void initializeHomePage() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.initializeHomePage();
            }
        });
    }

    public void onWidgetPositionChange(final int fromPosition, final int toPosition) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.onWidgetPositionChange(getWidgetsLiveMetadata().getValue());
            }
        });
    }

    public void addFolderAtPos(final int position, final Callback callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.addFolderAtPos(position, callback);

            }
        });
    }

    public String getJSonString(Class widgetMetadataClass, Object object) {
        return appsRepository.getJSonString(widgetMetadataClass, object);
    }

    public void removeFromHome(final String folderId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appsRepository.removeFromHome(folderId);
            }
        });
    }
}
