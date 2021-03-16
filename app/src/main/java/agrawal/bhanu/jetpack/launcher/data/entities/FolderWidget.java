package agrawal.bhanu.jetpack.launcher.data.entities;

import androidx.room.Embedded;

public class FolderWidget {

    @Embedded
    Folder folder;

    @Embedded(prefix = "type_")
    Widget widget;

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }
}
