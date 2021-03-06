package agrawal.bhanu.jetpack.launcher.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;

public class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private final Context context;
    private final LauncherViewModel mAppsModel;
    private ArrayList<App> apps;
    public static final int HOME = 0;
    public static final int ALL_APPS = 1;
    public static final int FOLDER = 2;
    public static final int FOLDER_DIALOG = 3;
    private int viewType;


    public ArrayList<App> getApps() {
        return apps;
    }

    public void setApps(ArrayList<App> apps) {
        this.apps = apps;
        Log.d("applistsize", String.valueOf(apps.size()));
        notifyDataSetChanged();
    }

    public AppsAdapter(Context context, ArrayList<App> apps, int viewType) {
        this.context = context;
        this.apps = apps;
        this.viewType = viewType;
        mAppsModel = ViewModelProviders.of((FragmentActivity)context).get(LauncherViewModel.class);
    }


    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (viewType){

            case ALL_APPS:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType, context);
            case HOME:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app_home, parent, false);
                return new AppViewHolder(itemView, viewType, context);
            case FOLDER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app_folder, parent, false);
                return new AppViewHolder(itemView, viewType, context);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType, context);

        }




    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        holder.appNameTV.setText(apps.get(position).getAppName());
        holder.appIconIV.setImageDrawable(mAppsModel.getAppIcon(apps.get(position).getAppPackage()));
        holder.setApp(apps.get(position));
    }



    @Override
    public int getItemCount() {
        return apps.size();
    }
}
