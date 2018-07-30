package agrawal.bhanu.jetpack.launcher.ui.defaultpage;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.AppContainer;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.Widget;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppsFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int APP_CONTAINER = 5;
    private final Context context;
    private final LauncherViewModel mAppsModel;
    private final FolderManager folderMananger;
    private ArrayList<Widget> appsFolder;
    public static final int FOLDER = 2;

    public ArrayList<Widget> getAppsFolder() {
        return appsFolder;
    }

    public void setAppsFolder(ArrayList<Widget> apps) {
        this.appsFolder = apps;
        notifyDataSetChanged();
    }

    public AppsFolderAdapter(Context context, ArrayList<Widget> appsFolder) {
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

            if(appsFolder.get(getAdapterPosition()).getRemovable() &&
                    !mAppsModel.getAppsByFolderId(appsFolder.get(getAdapterPosition()).getFolderId()).isEmpty()){
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

        if(appsFolder.get(position).getType().equals(Constants.APP_CONTAINER)){
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
            App app = mAppsModel.getAppByContainer(appsFolder.get(position).getAppContainerId());
            viewHolder.itemView.setVisibility(app == null?View.GONE:View.VISIBLE);
            if(app != null){
                viewHolder.appNameTV.setText(app.getAppName());
                viewHolder.appIconIV.setImageDrawable(app.getIcon());
                viewHolder.setApp(app);
            }
        }
        else{
            FolderViewHolder viewHolder = (FolderViewHolder)holder;
            viewHolder.bindTo(mAppsModel.getFolderById(appsFolder.get(position).getFolderId()));
        }
    }



    @Override
    public int getItemCount() {
        return appsFolder.size();
    }
}
