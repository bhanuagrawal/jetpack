package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys={@ForeignKey(
        entity = App.class,
        parentColumns = "appPackage",
        childColumns = "containerId",
        onDelete = CASCADE
)})
public class AppContainer {

    @PrimaryKey
    @ColumnInfo(name = "containerId")
    private String containerId;

    private String appId;

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public AppContainer(String containerId, String appId) {
        this.containerId = containerId;
        this.appId = appId;
    }
}
