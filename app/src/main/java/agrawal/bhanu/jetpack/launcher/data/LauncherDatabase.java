package agrawal.bhanu.jetpack.launcher.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import agrawal.bhanu.jetpack.launcher.data.dao.AppContainerDao;
import agrawal.bhanu.jetpack.launcher.data.dao.AppsDao;
import agrawal.bhanu.jetpack.launcher.data.dao.FolderAppsDao;
import agrawal.bhanu.jetpack.launcher.data.dao.FolderDao;
import agrawal.bhanu.jetpack.launcher.data.dao.WidgetPositionDao;
import agrawal.bhanu.jetpack.launcher.data.dao.WidgetsDao;
import agrawal.bhanu.jetpack.launcher.data.dao.HomePageMetadataDao;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderApps;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetPositon;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.data.util.Converters;

@Database(entities = {App.class,
        Folder.class,
        AppContainer.class,
        FolderApps.class,
        Widget.class,
        WidgetPositon.class}
        , version = 1)
@TypeConverters({Converters.class})
public abstract class LauncherDatabase extends RoomDatabase {

    public abstract AppsDao appsDao();
    public abstract FolderDao folderDao();
    public abstract AppContainerDao appContainerDao();
    public abstract FolderAppsDao folderAppsDao();
    public abstract WidgetsDao widgetsDao();
    public abstract HomePageMetadataDao HomePageMetadataDao();
    public abstract WidgetPositionDao WidgetPositionDao();

}
