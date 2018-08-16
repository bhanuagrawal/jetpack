package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderWidget;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM Folder")
    public Folder[] getAll();

    @Insert
    void insertAll(Folder... folders);

    @Insert
    void insert(Folder folder);

    @Delete
    void delete(Folder folder);


    @Query("SELECT folder.widgetId, folder.folderId, folder.folderName, widget.widgetId as type_widgetId, position as type_position, removable as type_removable, type as type_type from Folder left join widget on folder.widgetId = widget.widgetId where folderId = :folderId")
    LiveData<FolderWidget> getFolderById(String folderId);

    @Query("DELETE from Folder")
    void deleteAll();
}
