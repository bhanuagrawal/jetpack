package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderApps;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;

@Dao
public interface FolderAppsDao {

    @Insert
    void insertAll(FolderApps... folderApps);

    @Insert
    void insert(FolderApps folderApps);

    @Delete
    void delete(Widget widget);

    @Query("DELETE from FolderApps where appId = :appPackage")
    void delete(String appPackage);

    @Query("SELECT * from FolderApps inner join App on FolderApps.appId = App.appPackage where folderId=:folderId")
    ArrayList<App> getAppsByFolderId(String folderId);
}
