package agrawal.bhanu.jetpack.launcher.model;

import android.graphics.drawable.Drawable;

import java.util.Date;

import agrawal.bhanu.jetpack.reddit.model.Data;

public class AppDTO {
    
    private String appName;
    private String appPackage;
    private Drawable appIcon;
    private Date lastUsed;
    private int clicks;

    public AppDTO(AppDTO app) {
        appPackage = app.getAppPackage();
        lastUsed = app.getLastUsed();
        clicks = app.getClicks();
    }

    public AppDTO() {
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

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
