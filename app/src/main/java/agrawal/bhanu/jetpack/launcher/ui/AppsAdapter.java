package agrawal.bhanu.jetpack.launcher.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.R;

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private final Context context;
    private final LauncherViewModel mAppsModel;
    private ArrayList<AppDTO> apps;
    public static final int HOME = 0;
    public static final int ALL_APPS = 1;
    public static final int FOLDER = 2;
    public static final int FOLDER_DIALOG = 3;
    private int viewType;


    public ArrayList<AppDTO> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
        Log.d("applistsize", String.valueOf(apps.size()));
        notifyDataSetChanged();
    }

    public AppsAdapter(Context context, ArrayList<AppDTO> apps, int viewType) {
        this.context = context;
        this.apps = apps;
        this.viewType = viewType;
        mAppsModel = ViewModelProviders.of((FragmentActivity)context).get(LauncherViewModel.class);
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
                        intent.setData(Uri.parse("package:" + apps.get(position).getAppPackage()));
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
                Intent intent = pm.getLaunchIntentForPackage(apps.get(position).getAppPackage());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                if(intent == null){
                    throw new PackageManager.NameNotFoundException();
                }else{
                    mAppsModel.onAppSelected(apps.get(position));
                    context.startActivity(intent);
                }
            }catch(PackageManager.NameNotFoundException e){
            }
            catch(Exception e){
            }

        }
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
                return new AppViewHolder(itemView, viewType);
            case HOME:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app_home, parent, false);
                return new AppViewHolder(itemView, viewType);
            case FOLDER:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app_folder, parent, false);
                return new AppViewHolder(itemView, viewType);
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_app, parent, false);
                return new AppViewHolder(itemView, viewType);

        }




    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        holder.appNameTV.setText(apps.get(position).getAppName());
        holder.appIconIV.setImageDrawable(mAppsModel.getAppIcon(apps.get(position).getAppPackage()));
    }



    @Override
    public int getItemCount() {
        return apps.size();
    }
}
