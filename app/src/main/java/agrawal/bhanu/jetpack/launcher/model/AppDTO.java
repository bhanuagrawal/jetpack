package agrawal.bhanu.jetpack.launcher.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import agrawal.bhanu.jetpack.reddit.model.Data;

public class AppDTO extends AppsAndFolder implements Serializable {
    
    private String appName;
    private String appPackage;
    private Date lastUsed;
    private int clicks;
    private ArrayList<String> folderIds;

    public ArrayList<String> getFolderIds() {
        return folderIds;
    }

    public void setFolderIds(ArrayList<String> folderIds) {
        this.folderIds = folderIds;
    }

    public AppDTO(AppDTO app) {
        appPackage = app.getAppPackage();
        lastUsed = app.getLastUsed();
        clicks = app.getClicks();
        folderIds = app.getFolderIds();
        type = AppsAndFolder.APP;
    }

    public AppDTO() {
        type = AppsAndFolder.APP;
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
