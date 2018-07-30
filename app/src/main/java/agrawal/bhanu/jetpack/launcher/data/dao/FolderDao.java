package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.Folder;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM Folder")
    public LiveData<ArrayList<Folder>> getAll();

    @Insert
    void insertAll(Folder... folders);

    @Insert
    void insert(Folder folder);

    @Delete
    void delete(Folder folder);

    @Query("SELECT * from Folder where folderId = :folderId")
    Folder getFolderById(String folderId);
}
