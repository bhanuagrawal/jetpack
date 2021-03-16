package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.WidgetPositon;

@Dao
public interface WidgetPositionDao {


    @Insert
    void insertAll(WidgetPositon... widgetPositons);

    @Insert
    void insert(WidgetPositon widgetPositons);

}
