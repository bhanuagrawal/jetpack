package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.WidgetMetadata;

@Dao
public interface WidgetsMetadataDao {

    @Query("")
    LiveData<WidgetMetadata> getEmptyPositionOnDefaultPage();
}
