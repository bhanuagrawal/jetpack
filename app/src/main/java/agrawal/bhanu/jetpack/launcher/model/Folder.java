package agrawal.bhanu.jetpack.launcher.model;

import java.io.Serializable;

public class Folder extends AppsAndFolder implements Serializable{
    private String folderName;
    private String FolderId;

    public Folder() {
        folderName = "";
        FolderId = "";
        type = AppsAndFolder.FOLDER;
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
