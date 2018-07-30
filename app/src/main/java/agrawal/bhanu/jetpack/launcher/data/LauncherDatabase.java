package agrawal.bhanu.jetpack.launcher.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import agrawal.bhanu.jetpack.launcher.data.dao.AppContainerDao;
import agrawal.bhanu.jetpack.launcher.data.dao.AppsDao;
import agrawal.bhanu.jetpack.launcher.data.dao.FolderAppsDao;
import agrawal.bhanu.jetpack.launcher.data.dao.FolderDao;
import agrawal.bhanu.jetpack.launcher.data.dao.WidgetsDao;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderApps;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;

@Database(entities = {App.class,
        Folder.class,
        AppContainer.class,
        FolderApps.class,
        Widget.class}
        , version = 1)
public abstract class LauncherDatabase extends RoomDatabase {

    public abstract AppsDao appsDao();
    public abstract FolderDao folderDao();
    public abstract AppContainerDao appContainerDao();
    public abstract FolderAppsDao folderAppsDao();
    public abstract WidgetsDao widgetsDao();
}
