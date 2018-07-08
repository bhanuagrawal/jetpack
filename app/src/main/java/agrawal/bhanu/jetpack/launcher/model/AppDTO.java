package agrawal.bhanu.jetpack.launcher.model;

import android.graphics.drawable.Drawable;

public class AppDTO {
    
    private String appName;
    private String appPackage;
    private Drawable appIcon;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public void setAppName(String appName) {
        this.appName = appName;
        
    }
}
