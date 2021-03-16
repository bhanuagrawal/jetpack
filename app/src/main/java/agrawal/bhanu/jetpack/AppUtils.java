package agrawal.bhanu.jetpack;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;

public class AppUtils {

    public static ArrayList<App> getApps(AppsInfo appsInfo, int apps_per_page, int position) {
        return new ArrayList<App>(appsInfo.getApps().subList(position * apps_per_page, appsInfo.getApps().size() > position * apps_per_page + apps_per_page ? position * apps_per_page + apps_per_page : appsInfo.getApps().size()));
    }

    public static int getNoOfPages(AppsInfo appsInfo, int appsPerPage) {
        return (int) Math.ceil((float) appsInfo.getApps().size() / appsPerPage);
    }

    public static boolean checkIfAlreadyhavePermission(Application application) {
        int result = ContextCompat.checkSelfPermission(application.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }
}
