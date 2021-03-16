package agrawal.bhanu.jetpack.launcher.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys={@ForeignKey(
        entity = App.class,
        parentColumns = "appPackage",
        childColumns = "appId",
        onDelete = CASCADE
), @ForeignKey(
        entity = Widget.class,
        parentColumns = "widgetId",
        childColumns = "widgetId",
        onDelete = CASCADE
)},  indices = {@Index(value = {"widgetId"},
        unique = true)})

public class AppContainer {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "containerId")
    @NonNull
    private int containerId;

    private String appId;

    private long widgetId;


    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getContainerId() {
        return containerId;
    }

    public void setContainerId(int containerId) {
        this.containerId = containerId;
    }

    public AppContainer(String appId, long widgetId) {
        this.appId = appId;
        this.widgetId = widgetId;
    }
}
