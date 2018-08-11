package agrawal.bhanu.jetpack.launcher.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetPositon;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback;

@Dao
public abstract class HomePageMetadataDao {

    @Query("select AppContainer.appId as appId, widgets.widgetId as widgetId, widgets.position as position, removable, AppContainer.containerId as appContainerId,  folders.folderId as folderId, folders.appsCount as appsCount, type from " +
            "(Select widgetId, Widget.position, removable, type from WidgetPositon left join Widget on WidgetPositon.position = Widget.position) widgets left join " +
            "AppContainer on widgets.widgetId = AppContainer.widgetId left join" +
            "(select Folder.folderId, widgetId, count(FolderApps.appId) as appsCount from folder left join FolderApps on Folder.folderId = FolderApps.folderId group by Folder.folderId) folders on " +
            "widgets.widgetId = folders.widgetId order by widgets.position")
    public abstract LiveData<List<WidgetsMetaData>> getWidgetsLiveMetaData();



    @Insert
    abstract long insertWidgetPosition(WidgetPositon widgetPositons);


    @Insert
    abstract long insertWidget(Widget widget);

    @Insert
    abstract void insertFolder(Folder folder);

    @Insert
    abstract long insertAppContainer(AppContainer appContainer);

    @Query("SELECT * from Widget where position = :posiition")
    abstract Widget getWidgetAtPosition(int posiition);

    @Update
    abstract void updateWidget(Widget widget);

    @Delete
    abstract void deleteWidget(Widget widget);

    @Query("Select Folder.*  from folder left join widget on folder.widgetId = widget.widgetId where position = :position")
    abstract Folder getFolderAtPos(int position);

    @Transaction
    public void createFolderWidget(int position, String type, boolean removable, String folderId, String folderName){
        insertFolder(new Folder(folderId, folderName, insertWidget(new Widget((int) insertWidgetPosition(new WidgetPositon(position)), type, removable))));
    }

    @Transaction
    public void insertAppAtPosition(App app, int position){

        Widget widget = getWidgetAtPosition(position);
        widget.setType(Constants.APP_CONTAINER);
        widget.setRemovable(true);
        updateWidget(widget);
        insertAppContainer(new AppContainer(app.getAppPackage(), widget.getWidgetId()));
    }

    @Transaction
    public void createEmptyWidget(int position, String type) {
        insertWidget(new Widget((int) insertWidgetPosition(new WidgetPositon(position)), type, false));
    }

    @Transaction
    public void removeFromHome(int position){
        Widget widget = getWidgetAtPosition(position);
        deleteWidget(widget);
        widget.setType(Constants.EMPTY);
        widget.setRemovable(false);
        insertWidget(widget);
    }

    @Transaction
    public void onWidgetPositionChange(int fromPosition, int toPosition) {
        Widget w1 = getWidgetAtPosition(fromPosition);
        Widget w2 = getWidgetAtPosition(toPosition);
        w1.setPosition(toPosition);
        w2.setPosition(fromPosition);
        updateWidget(w1);
        updateWidget(w2);


    }

    @Transaction
    public void onWidgetPositionChange(List<WidgetsMetaData> value) {
        int i=0;
        for(WidgetsMetaData widgetsMetaData: value){
            updateWidget(widgetsMetaData.getWidgetId(), i);
            i++;
        }

    }

    @Query("UPDATE Widget set position = :i where  widgetId = :widgetId")
    protected abstract void updateWidget(long widgetId, int i);

    @Transaction
    public void addFolderAtPos(int position, Callback callback){
        Widget w1 = getWidgetAtPosition(position);
        deleteWidget(w1);
        insertFolder(new Folder(UUID.randomUUID().toString(), "New Folder", insertWidget(new Widget(position, Constants.FOLDER, true))));
        callback.onSuccess();
    }
}
