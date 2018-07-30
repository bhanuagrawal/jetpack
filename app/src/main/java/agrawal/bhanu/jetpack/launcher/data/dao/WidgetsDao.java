package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.data.entities.Widget;

@Dao
public interface WidgetsDao {

    @Query("SELECT * FROM Widget")
    public ArrayList<Widget> getAll();

    @Insert
    void insertAll(Widget... widgets);

    @Delete
    void delete(Widget widget);

    @Query("DELETE from Widget")
    void deleteAll();
}
