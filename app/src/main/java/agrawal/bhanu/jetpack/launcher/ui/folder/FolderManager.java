package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

public class FolderManager  {

    Context context;

    public FolderManager(Context context) {
        this.context = context;
    }

    public AppsFolder newFolder(int folderContainer, String folderId) {



        AppsFolder appsFolderFragment = AppsFolder.newInstance(folderContainer, "", folderId);
        ((AppCompatActivity)context).getSupportFragmentManager().
                beginTransaction().
                replace(folderContainer, appsFolderFragment, folderId).
                commit();

        return appsFolderFragment;
    }

}
