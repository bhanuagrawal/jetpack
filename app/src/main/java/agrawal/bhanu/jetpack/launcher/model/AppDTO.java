package agrawal.bhanu.jetpack.launcher.model;

import android.graphics.drawable.Drawable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import agrawal.bhanu.jetpack.reddit.model.Data;

public class AppDTO implements Serializable {
    
    private String appName;
    private String appPackage;
    private Date lastUsed;
    private int clicks;
    private ArrayList<String> folderIds;
    private boolean isDefault;

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    private Drawable icon;

    public ArrayList<String> getFolderIds() {
        if(folderIds == null){
            folderIds = new ArrayList<>();
        }
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
