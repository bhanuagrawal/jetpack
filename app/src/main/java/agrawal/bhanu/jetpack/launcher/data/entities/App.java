package agrawal.bhanu.jetpack.launcher.data.entities;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Date;

@Entity
public class App {

    @PrimaryKey
    @ColumnInfo(name = "appPackage")
    private String appPackage;

    @ColumnInfo(name = "appName")
    private String appName;

    @ColumnInfo(name = "lastUsed")
    private Date lastUsed;

    @ColumnInfo(name = "clioks")
    private int clicks;

    @ColumnInfo(name = "isDefault")
    private boolean isDefault;

    @Ignore
    private Drawable icon;

    public App(App app) {
        this.appName = app.appName;
        this.appPackage = app.appPackage;
        this.lastUsed = app.lastUsed;
        this.clicks = app.clicks;
        this.isDefault = app.isDefault;
        this.icon = app.icon;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
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

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
