package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import android.database.sqlite.SQLiteConstraintException;

import java.util.List;

import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderApps;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback;

@Dao
public abstract class FolderAppsDao extends HomePageMetadataDao{

    @Insert
    public abstract void insertAll(FolderApps... folderApps);

    @Insert
    public abstract void insert(FolderApps folderApps);

    @Delete
    public abstract void delete(Widget widget);

    @Query("DELETE from FolderApps where appId = :appPackage")
    public abstract void delete(String appPackage);

    @Query("SELECT App.* from FolderApps inner join App on FolderApps.appId = App.appPackage where folderId=:folderId order by lastUsed desc")
    public abstract LiveData<List<App>> getAppsByFolderId(String folderId);

    @Transaction
    public void addToFolder(int position, String appId, Callback callback){
        Folder folder = getFolderAtPos(position);
        try {
            insert(new FolderApps(folder.getFolderId(), appId));
            callback.onSuccess();
        }
        catch (SQLiteConstraintException e){
            callback.onError("App already present in the folder");
        }

    }
}
