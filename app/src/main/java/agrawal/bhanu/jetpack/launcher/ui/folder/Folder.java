package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import agrawal.bhanu.jetpack.MainActivity;

public class Folder implements LifecycleObserver {



    public static AppsFolder newFolder(Bundle savedInstanceState, Context context, int folderContainer, String folderName, String folderId) {

        if(savedInstanceState != null){
            return (AppsFolder) ((FragmentActivity)context).getSupportFragmentManager().findFragmentByTag(folderId);
        }

        AppsFolder appsFolderFragment = AppsFolder.newInstance(folderContainer, folderName, folderId);
        ((FragmentActivity)context).getSupportFragmentManager().
                beginTransaction().
                replace(folderContainer, appsFolderFragment, MainActivity.FREQUENT_APPS).
                commit();

        return appsFolderFragment;
    }
}
