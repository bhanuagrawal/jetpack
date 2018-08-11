package agrawal.bhanu.jetpack.launcher.data.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys={@ForeignKey(
        entity = Widget.class,
        parentColumns = "widgetId",
        childColumns = "widgetId",
        onDelete = CASCADE
)},  indices = {@Index(value = {"widgetId"},
        unique = true)})
public class Folder {

    @PrimaryKey
    @ColumnInfo(name = "folderId")
    @NonNull
    private String folderId;

    @ColumnInfo(name = "folderName")
    private String folderName;

    @ColumnInfo(name = "widgetId")
    private long widgetId;

    public Folder(String folderId, String folderName, long widgetId) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.widgetId = widgetId;
    }

    public long getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(long widgetId) {
        this.widgetId = widgetId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
