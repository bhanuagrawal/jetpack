package agrawal.bhanu.jetpack.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.R;

public class AllAppsAdapter extends RecyclerView.Adapter<AllAppsAdapter.AppViewHolder> {

    private final Context context;
    private ArrayList<AppDTO> apps;

    public ArrayList<AppDTO> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView appNameTV;
        public ImageView appIconIV;
        public ConstraintLayout parentLayout;

        public AppViewHolder(View view) {
            super(view);
            appNameTV = (TextView) view.findViewById(R.id.app_name);
            appIconIV = (ImageView) view.findViewById(R.id.app_icon);
            parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            PackageManager pm = context.getPackageManager();
            try{
                Intent intent = pm.getLaunchIntentForPackage(apps.get(position).getAppPackage());
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                if(intent == null){
                    throw new PackageManager.NameNotFoundException();
                }else{
                    context.startActivity(intent);
                }
            }catch(PackageManager.NameNotFoundException e){
            }
        }
    }


    public AllAppsAdapter(Context context, ArrayList<AppDTO> apps) {
        this.context = context;
        this.apps = apps;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_app, parent, false);

        return new AppViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {

        holder.appNameTV.setText(apps.get(position).getAppName());
        holder.appIconIV.setImageDrawable(apps.get(position).getAppIcon());
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }
}
