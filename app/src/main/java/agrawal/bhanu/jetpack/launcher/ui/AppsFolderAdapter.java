package agrawal.bhanu.jetpack.launcher.ui;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.Folder;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppsFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final LauncherViewModel mAppsModel;
    private final FolderManager folderMananger;
    private ArrayList<Object> appsFolder;
    public static final int HOME = 0;
    public static final int ALL_APPS = 1;
    public static final int FOLDER = 2;
    public static final int FOLDER_DIALOG = 3;


    public void setAppsFolder(ArrayList<Object> apps) {
        this.appsFolder = apps;
        notifyDataSetChanged();
    }

    public AppsFolderAdapter(Context context, ArrayList<Object> appsFolder) {
        this.context = context;
        this.appsFolder = appsFolder;
        mAppsModel = ViewModelProviders.of((FragmentActivity)context).get(LauncherViewModel.class);
        folderMananger = new FolderManager(context);
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView appNameTV;
        public ImageView appIconIV;
        public ConstraintLayout parentLayout;
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        private PackageManager pm;

        public AppViewHolder(View view, int viewType) {
            super(view);
            appNameTV = (TextView) view.findViewById(R.id.app_name);
            appNameTV.setVisibility(viewType == ALL_APPS || viewType == FOLDER_DIALOG? View.VISIBLE: View.GONE);
            appIconIV = (ImageView) view.findViewById(R.id.app_icon);
            parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
            if(viewType != FOLDER){
                parentLayout.setOnClickListener(this);
                parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        int position = getAdapterPosition();
                        intent.setData(Uri.parse("package:" + ((AppDTO)appsFolder.get(position)).getAppPackage()));
                        context.startActivity(intent);
                        return true;
                    }
                });
            }

        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            pm = context.getPackageManager();
            try{
                if(getItemViewType() == FOLDER_DIALOG){
                    ((Activity)context).onBackPressed();
                }
                Intent intent = pm.getLaunchIntentForPackage(((AppDTO)appsFolder.get(position)).getAppPackage());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                if(intent == null){
                    throw new PackageManager.NameNotFoundException();
                }else{
                    mAppsModel.onAppSelected(((AppDTO)appsFolder.get(position)));
                    context.startActivity(intent);
                }
            }catch(PackageManager.NameNotFoundException e){
            }
            catch(Exception e){
            }

        }
    }


    public class FolderViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.folderContainer)
        FrameLayout frameLayout;

        public FolderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
/*            Folder folder = (Folder) appsFolder.get(getAdapterPosition());
            frameLayout.setVisibility(mAppsModel.getAppsByFolderId(folder.getFolderId()).isEmpty()?View.INVISIBLE:View.VISIBLE);*/
        }

        public void bindTo(Folder folder) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {

                if(folder.getFolderId() != null){
                    int newContainerId = View.generateViewId();
                    frameLayout.setId(newContainerId);
                    folderMananger.newFolder(frameLayout.getId(), folder.getFolderName(), folder.getFolderId());
                }
            }

        }
    }


    @Override
    public int getItemViewType(int position) {

        if(appsFolder.get(position) instanceof AppDTO){
            return ALL_APPS;
        }
        else{
            return FOLDER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType){

            case ALL_APPS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType);
            case FOLDER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_folder, parent, false);
                return new FolderViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType);

        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder.getItemViewType() ==  ALL_APPS){

            AppViewHolder viewHolder = (AppViewHolder)holder;
            AppDTO app = (AppDTO)appsFolder.get(position);
            viewHolder.appNameTV.setText(app.getAppName());
            viewHolder.appIconIV.setImageDrawable(app.getAppIcon());
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
