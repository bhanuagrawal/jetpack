package agrawal.bhanu.jetpack.launcher.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;

@Dao
public interface AppContainerDao {

    @Insert
    void insertAll(AppContainer... appContainers);

    @Insert
    void insert(AppContainer appContainer);

    @Delete
    void delete(Widget widget);

    @Query("SELECT * from AppContainer inner join App on AppContainer.appId = App.appPackage where containerId = :containerId")
    LiveData<App> getAppBYContainerId(String containerId);


}
