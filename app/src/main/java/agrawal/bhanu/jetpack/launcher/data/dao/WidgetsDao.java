package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.Widget;

@Dao
public interface WidgetsDao {

    @Query("SELECT * FROM Widget")
    public Widget[] getAll();

    @Insert
    void insertAll(Widget... widgets);

    @Insert
    long insert(Widget widget);

    @Delete
    void delete(Widget widget);

    @Query("DELETE from Widget")
    void deleteAll();


}
