package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetPositon;

@Dao
public interface WidgetPositionDao {


    @Insert
    void insertAll(WidgetPositon... widgetPositons);

    @Insert
    void insert(WidgetPositon widgetPositons);

}
