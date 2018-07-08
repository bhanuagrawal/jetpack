package agrawal.bhanu.jetpack;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;

public class AppUtils {

    public static ArrayList<AppDTO> getApps(AppsInfo appsInfo, int position) {
        return new ArrayList<AppDTO>(appsInfo.getApps().subList(position * appsInfo.getApps_per_page(), appsInfo.getApps().size() > position * appsInfo.getApps_per_page() + appsInfo.getApps_per_page() ? position * appsInfo.getApps_per_page() + appsInfo.getApps_per_page() : appsInfo.getApps().size()));
    }

    public static int getNoOfPages(AppsInfo appsInfo) {
        return (int) Math.ceil((float) appsInfo.getApps().size() / appsInfo.getApps_per_page());
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
