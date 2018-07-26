package agrawal.bhanu.jetpack.launcher.model;

import java.io.Serializable;

public class Folder extends AppsAndFolder implements Serializable{
    private String folderName;
    private String FolderId;
    private Boolean removable;

    public Boolean getRemovable() {
        return removable;
    }

    public void setRemovable(Boolean removable) {
        this.removable = removable;
    }

    public Folder(String folderName, String FolderId) {
        super(AppsAndFolder.FOLDER);
        this.folderName = folderName;
        this.FolderId = FolderId;
        this.type = AppsAndFolder.FOLDER;
        this.removable = true;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderId() {
        return FolderId;
    }

    public void setFolderId(String folderId) {
        FolderId = folderId;
    }

}
