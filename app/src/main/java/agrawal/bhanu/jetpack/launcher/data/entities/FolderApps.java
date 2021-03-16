package agrawal.bhanu.jetpack.launcher.data.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = Folder.class,
                parentColumns = "folderId",
                childColumns = "folderId",
                onDelete = CASCADE
        ),
        @ForeignKey(
                entity = App.class,
                parentColumns = "appPackage",
                childColumns = "appId",
                onDelete = CASCADE
        )
}, indices = {@Index(value = {"folderId", "appId"},
        unique = true)})
public class FolderApps {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "folderId")
    private String folderId;

    @ColumnInfo(name = "appId")
    private String appId;

    public FolderApps(String folderId, String appId) {
        this.folderId = folderId;
        this.appId = appId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
