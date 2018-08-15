package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;


public class WidgetsMetaData {

    @ColumnInfo(name = "widgetId")
    private long widgetId;
    @ColumnInfo(name = "appContainerId")
    private String appContainerId;
    @ColumnInfo(name = "folderId")
    private String folderId;
    @ColumnInfo(name = "position")
    private int position;
    @ColumnInfo(name = "removable")
    private boolean removable;
    @ColumnInfo(name = "appsCount")
    private int appsCount;
    @ColumnInfo(name = "type")
    private String type;

    @ColumnInfo(name = "appId")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getWidgetId() {
        return widgetId;
    }


    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public String getAppContainerId() {
        return appContainerId;
    }

    public void setAppContainerId(String appContainerId) {
        this.appContainerId = appContainerId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public int getAppsCount() {
        return appsCount;
    }

    public void setAppsCount(int appCount) {
        this.appsCount = appCount;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}