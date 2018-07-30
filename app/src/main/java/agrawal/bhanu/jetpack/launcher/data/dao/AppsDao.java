package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.App;

@Dao
public interface AppsDao {

    @Query("SELECT * FROM App")
    public ArrayList<App> getAll();

    @Insert
    public void insertAll(App... apps);

    @Delete
    public void delete(App app);

    @Query("SELECT * FROM FolderApps left join App on FolderApps.appId = App.appPackage " +
            "where FolderApps.folderId = :folderId")
    public ArrayList<App> getAppsByFolderId(String folderId);

    @Query("SELECT * FROM AppContainer inner join App on AppContainer.appId = App.appPackage " +
            "where AppContainer.containerId = :containerId")
    public App getAppsByContainerId(String containerId);

    @Query("SELECT * FROM App where appPackage = :packageName")
    public App getAppByPackagege(String packageName);
}
