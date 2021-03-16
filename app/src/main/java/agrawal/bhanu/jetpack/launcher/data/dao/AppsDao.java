package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import agrawal.bhanu.jetpack.launcher.data.entities.App;

@Dao
public interface AppsDao {

    @Query("SELECT * FROM App")
    public App[] getAll();

    @Insert
    public void insertAll(App... apps);

    @Delete
    public void delete(App app);

    @Query("SELECT * FROM FolderApps left join App on FolderApps.appId = App.appPackage " +
            "where FolderApps.folderId = :folderId")
    public App[] getAppsByFolderId(String folderId);

    @Query("SELECT * FROM AppContainer inner join App on AppContainer.appId = App.appPackage " +
            "where AppContainer.containerId = :containerId")
    public App getAppsByContainerId(String containerId);

    @Query("SELECT * FROM App where appPackage = :packageName")
    public App getAppByPackagege(String packageName);

    @Update
    void update(App app);

    @Query("DELETE from App")
    void deleteAll();

    @Query("Delete from app where appPackage not in (:appPackages)")
    void deleteUninstalled(String[] appPackages);
}
