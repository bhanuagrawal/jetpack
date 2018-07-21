package agrawal.bhanu.jetpack.launcher;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.ui.AppsFolder;

public class FolderView extends FrameLayout{

    public static final int EMPTY_VISIBLE = 0;
    public static final int EMPTY_HIDDEN = 1;
    private static final String APPS_FRAGMENT = "asersgdhfg";
    private AppsFolder appFolderFragment;
    private int type;
    private ArrayList<AppDTO> apps;
    private String folderName;

    public FolderView(@NonNull Context context) {
        super(context);
    }


    public FolderView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }



    public FolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FolderView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setType(int type) {
        this.type = type;
    }



    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
    }

    public void setName(String folderName) {
        this.folderName = folderName;
    }
}
