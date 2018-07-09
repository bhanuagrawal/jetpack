package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;


import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.AppsRepository;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.MyApp;

public class AppsViewModel extends AndroidViewModel {

    private MutableLiveData<AppsInfo> mCurrentApps;
    @Inject
    AppsRepository appsRepository;

    @Inject
    Executor executor;


    public AppsViewModel(@NonNull Application application) {
        super(application);
        ((MyApp)application).getLocalDataComponent().inject(this);
    }

    public MutableLiveData<AppsInfo> getAppsInfo() {

        if (mCurrentApps == null) {
            mCurrentApps = new MutableLiveData<AppsInfo>();
            mCurrentApps.setValue(new AppsInfo(0, 1, 0, new ArrayList<AppDTO>()));
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
            mCurrentApps.setValue(new AppsInfo(0, 1, 0, new ArrayList<AppDTO>()));
        }
        appsRepository.fetchApps(mCurrentApps);
    }
}
