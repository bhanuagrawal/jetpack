package agrawal.bhanu.jetpack.launcher.ui;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.databinding.RowAppBinding;
import agrawal.bhanu.jetpack.databinding.RowAppFolderBinding;
import agrawal.bhanu.jetpack.databinding.RowAppHomeBinding;
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
        mAppsModel = ViewModelProviders.of((AppCompatActivity)context).get(LauncherViewModel.class);
    }


    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        switch (viewType){

            case ALL_APPS:
                return new AppViewHolder(RowAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), viewType, context);
            case HOME:
                return new AppViewHolder(RowAppHomeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), viewType, context);
            case FOLDER:
                return new AppViewHolder(RowAppFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), viewType, context);

            default:
                return new AppViewHolder(RowAppBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), viewType, context);


        }




    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        ((TextView)holder.itemView.findViewById(R.id.app_name)).setText(apps.get(position).getAppName());
        ((ImageView)holder.itemView.findViewById(R.id.app_icon)).setImageDrawable(mAppsModel.getAppIcon(apps.get(position).getAppPackage()));
        holder.setApp(apps.get(position));
    }



    @Override
    public int getItemCount() {
        return apps.size();
    }
}
