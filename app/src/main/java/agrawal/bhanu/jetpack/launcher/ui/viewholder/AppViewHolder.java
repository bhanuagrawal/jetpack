package agrawal.bhanu.jetpack.launcher.ui.viewholder;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppContainer;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.AppsFolderAdapter;
import agrawal.bhanu.jetpack.launcher.util.callbacks.AddToHomeCallback;

public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnCreateContextMenuListener {

    private final Context context;
    private final LauncherViewModel mAppsModel;
    public TextView appNameTV;
    public ImageView appIconIV;
    public ConstraintLayout parentLayout;
    private AppDTO app;
    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    private PackageManager pm;

    public AppViewHolder(View view, int viewType, Context context) {
        super(view);
        if(viewType != AppsAdapter.FOLDER){
            view.setOnCreateContextMenuListener(this);
        }
        appNameTV = (TextView) view.findViewById(R.id.app_name);
        appNameTV.setVisibility(viewType == AppsAdapter.ALL_APPS || viewType == AppsAdapter.FOLDER_DIALOG || viewType == AppsFolderAdapter.APP_CONTAINER? View.VISIBLE: View.GONE);
        appIconIV = (ImageView) view.findViewById(R.id.app_icon);
        parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
        if(viewType != AppsAdapter.FOLDER){
            parentLayout.setOnClickListener(this);
        }
        this.context = context;
        mAppsModel = ViewModelProviders.of((FragmentActivity)context).get(LauncherViewModel.class);


    }

    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        pm = context.getPackageManager();
        try{
            if(getItemViewType() == AppsAdapter.FOLDER_DIALOG){
                ((Activity)context).onBackPressed();
            }
            Intent intent = pm.getLaunchIntentForPackage(app.getAppPackage());
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            if(intent == null){
                throw new PackageManager.NameNotFoundException();
            }else{
                mAppsModel.onAppSelected(app);
                context.startActivity(intent);
            }
        }catch(PackageManager.NameNotFoundException e){
        }
        catch(Exception e){
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        if(getItemViewType() == AppsAdapter.ALL_APPS){
            MenuItem add_to_home = contextMenu.add("Add To Home");
            add_to_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    mAppsModel.addToHome(app, mAppsModel.getEmptyPositionOnDefaultPage(), new AddToHomeCallback() {
                        @Override
                        public void onSuccess() {
                            ((FragmentActivity)context).onBackPressed();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        }
                    });
                    return false;
                }
            });
        }
        if(getItemViewType() == AppsFolderAdapter.APP_CONTAINER){

            MenuItem add_to_home = contextMenu.add("Remove");
            add_to_home.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    mAppsModel.removeFromHome(getAdapterPosition());
                    return false;
                }
            });

        }


        MenuItem appInfo = contextMenu.add("Info/Uninstall");
        appInfo.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position = getAdapterPosition();
                intent.setData(Uri.parse("package:" + app.getAppPackage()));
                context.startActivity(intent);
                return false;
            }
        });
    }


    public void setApp(AppDTO app) {
        this.app = app;
    }
}