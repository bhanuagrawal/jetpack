package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class FolderManager  {

    Context context;

    public FolderManager(Context context) {
        this.context = context;
    }

    public AppsFolder newFolder(int folderContainer, String folderName, String folderId) {



        AppsFolder appsFolderFragment = AppsFolder.newInstance(folderContainer, folderName, folderId);
        ((FragmentActivity)context).getSupportFragmentManager().
                beginTransaction().
                replace(folderContainer, appsFolderFragment, folderId).
                commit();

        return appsFolderFragment;
    }

}
