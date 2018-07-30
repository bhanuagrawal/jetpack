package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import agrawal.bhanu.jetpack.Constants;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Folder.class,
                parentColumns = "folderId",
                childColumns = "folderId",
                onDelete = CASCADE
        ),
        @ForeignKey(
                entity = AppContainer.class,
                parentColumns = "containerId",
                childColumns = "appContainerId",
                onDelete = CASCADE
        )}, indices = {@Index(value = {"position"},
        unique = true)})
public class Widget {

    @PrimaryKey(autoGenerate = true)
    String id;

    @ColumnInfo(name = "appContainerId")
    String appContainerId;

    @ColumnInfo(name = "folderId")
    String folderId;

    @ColumnInfo(name = "type")
    String type;


    @ColumnInfo(name = "position")
    int position;

    @ColumnInfo(name = "removable")
    private Boolean removable;

    public Widget(String appContainerId, String folderId, String type, boolean removable, int position) {
        this.appContainerId = appContainerId;
        this.folderId = folderId;
        this.type = type;
        this.removable = removable;
        this.position = position;
    }

    public Widget(AppContainer appContainer, int position) {
        this.appContainerId = appContainer.getContainerId();
        this.type = Constants.APP_CONTAINER;
        this.position = position;
        this.removable = true;
    }

    public Boolean getRemovable() {
        return removable;
    }

    public void setRemovable(Boolean removable) {
        this.removable = removable;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
