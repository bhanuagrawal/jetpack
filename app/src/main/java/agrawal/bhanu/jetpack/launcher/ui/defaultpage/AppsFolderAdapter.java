package agrawal.bhanu.jetpack.launcher.ui.defaultpage;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppContainer;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsAndFolder;
import agrawal.bhanu.jetpack.launcher.model.Folder;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;
import agrawal.bhanu.jetpack.launcher.util.callbacks.AddToHomeCallback;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppsFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int APP_CONTAINER = 5;
    private final Context context;
    private final LauncherViewModel mAppsModel;
    private final FolderManager folderMananger;
    private ArrayList<AppsAndFolder> appsFolder;
    public static final int FOLDER = 2;

    public ArrayList<AppsAndFolder> getAppsFolder() {
        return appsFolder;
    }

    public void setAppsFolder(ArrayList<AppsAndFolder> apps) {
        this.appsFolder = apps;
        notifyDataSetChanged();
    }

    public AppsFolderAdapter(Context context, ArrayList<AppsAndFolder> appsFolder) {
        this.context = context;
        this.appsFolder = appsFolder;
        mAppsModel = ViewModelProviders.of((FragmentActivity)context).get(LauncherViewModel.class);
        folderMananger = new FolderManager(context);
    }



    public class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{

        @BindView(R.id.folderContainer)
        FrameLayout frameLayout;

        public FolderViewHolder(View view) {
            super(view);
            view.setOnCreateContextMenuListener(this);
            ButterKnife.bind(this, view);
        }

        public void bindTo(Folder folder) {
            itemView.setVisibility(mAppsModel.getAppsByFolderId(folder.getFolderId()).isEmpty()?View.INVISIBLE:View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {

                if(folder.getFolderId() != null && !folder.getFolderId().isEmpty()){
                    int newContainerId = View.generateViewId();
                    frameLayout.setId(newContainerId);
                    folderMananger.newFolder(frameLayout.getId(), folder.getFolderName(), folder.getFolderId());
                }
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            if(((Folder)appsFolder.get(getAdapterPosition())).getRemovable() &&
                    !((Folder)appsFolder.get(getAdapterPosition())).getFolderId().isEmpty()){
                MenuItem add_to_home = contextMenu.add("Remove");
                add_to_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        mAppsModel.removeFromHome(getAdapterPosition());
                        return false;
                    }
                });
            }
        }
    }


    @Override
    public int getItemViewType(int position) {

        if(appsFolder.get(position) instanceof AppContainer){
            return APP_CONTAINER;
        }
        else{
            return FOLDER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType){

            case APP_CONTAINER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app_container, parent, false);
                return new AppViewHolder(itemView, viewType, context);
            case FOLDER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_folder, parent, false);
                return new FolderViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType, context);

        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() ==  APP_CONTAINER){

            AppViewHolder viewHolder = (AppViewHolder)holder;
            AppDTO app = mAppsModel.getAppByContainer(((AppContainer)appsFolder.get(position)));
            viewHolder.itemView.setVisibility(app == null?View.GONE:View.VISIBLE);
            if(app != null){
                viewHolder.appNameTV.setText(app.getAppName());
                viewHolder.appIconIV.setImageDrawable(app.getIcon());
                viewHolder.setApp(app);
            }
        }
        else{
            FolderViewHolder viewHolder = (FolderViewHolder)holder;
            viewHolder.bindTo((Folder)appsFolder.get(position));
        }
    }



    @Override
    public int getItemCount() {
        return appsFolder.size();
    }
}
