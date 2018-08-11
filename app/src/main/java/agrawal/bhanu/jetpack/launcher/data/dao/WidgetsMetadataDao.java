package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.WidgetMetadata;

@Dao
public interface WidgetsMetadataDao {

    @Query("")
    LiveData<WidgetMetadata> getEmptyPositionOnDefaultPage();
}
