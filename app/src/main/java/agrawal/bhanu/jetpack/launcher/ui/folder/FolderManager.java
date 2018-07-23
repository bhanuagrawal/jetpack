package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.model.Folder;

public class FolderManager  {

    ArrayList<AppsFolder> folderFragments;
    private ArrayList<Folder> folders;
    Context context;

    public FolderManager(Context context) {
        this.context = context;
        folders = new ArrayList<>();
        folderFragments = new ArrayList<>();
    }

    public AppsFolder newFolder(int folderContainer, String folderName, String folderId) {



        AppsFolder appsFolderFragment = AppsFolder.newInstance(folderContainer, folderName, folderId);
        ((FragmentActivity)context).getSupportFragmentManager().
                beginTransaction().
                replace(folderContainer, appsFolderFragment, folderId).
                commit();

        return appsFolderFragment;
    }

    public void setFolders(ArrayList<Folder> folders) {
        this.folders = folders;
        removeOldFolders();
        addFolders(folders);
    }

    private void addFolders(ArrayList<Folder> folders) {
        for(Folder folder: folders){
            folderFragments.add(newFolder(folder.getFolderContainer(), folder.getFolderName(), folder.getFolderId()));
        }
    }

    private void removeOldFolders() {
        for(Fragment folderFragment: folderFragments){
            ((FragmentActivity)context).getSupportFragmentManager().
                    beginTransaction().remove(folderFragment).commit();
        }

        folderFragments.clear();
    }
}
