package agrawal.bhanu.jetpack.launcher.model;

public class Folder {
    private String folderName;
    private String FolderId;
    private int folderContainer;

    public int getFolderContainer() {
        return folderContainer;
    }

    public void setFolderContainer(int folderContainer) {
        this.folderContainer = folderContainer;
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
