package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Application;
import android.app.WallpaperManager;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.animation.Transformation;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.reddit.model.Data;

public class AppsViewModel extends AndroidViewModel {

    private MutableLiveData<AppsInfo> mCurrentApps;
    private MutableLiveData<Drawable> wallpaper;
    private LiveData<ArrayList<AppDTO>> appSuggestions;
    private Application application;
    @Inject
    AppsRepository appsRepository;

    public LiveData<ArrayList<AppDTO>> getAppSuggestions() {

        if(appSuggestions == null){
            appSuggestions = Transformations.map(mCurrentApps, new Function<AppsInfo, ArrayList<AppDTO>>() {
                @Override
                public ArrayList<AppDTO> apply(final AppsInfo input) {
                    return appsRepository.fetchAppSuggestions(input);
                }
            });
        }
        return appSuggestions;

    }

    @Inject
    Executor executor;

    @Inject
    WallpaperManager wallpaperManager;


    public AppsViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        ((MyApp)application).getLocalDataComponent().inject(this);
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
        getAppsInfo().setValue(getAppsInfo().getValue());
        saveAppsUsageInfo();
    }

    public void saveAppsUsageInfo() {
        appsRepository.saveAppsUsageInfo(getAppsInfo().getValue().getApps());

    }
}
