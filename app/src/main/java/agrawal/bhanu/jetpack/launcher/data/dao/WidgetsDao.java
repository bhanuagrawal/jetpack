package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

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
