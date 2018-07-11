package agrawal.bhanu.jetpack.launcher.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppViewHolder> {

    private final Context context;
    private ArrayList<AppDTO> apps;
    public static final int HOME = 0;
    public static final int ALL_APPS = 1;
    private int viewType;


    public ArrayList<AppDTO> getApps() {
        return apps;
    }

    public void setApps(ArrayList<AppDTO> apps) {
        this.apps = apps;
        notifyDataSetChanged();
    }

    public AppsAdapter(Context context, ArrayList<AppDTO> apps, int viewType) {
        this.context = context;
        this.apps = apps;
        this.viewType = viewType;
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
            appNameTV.setVisibility(viewType == HOME? View.GONE: View.VISIBLE);
            appIconIV = (ImageView) view.findViewById(R.id.app_icon);
            parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
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

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            pm = context.getPackageManager();
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


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(viewType==HOME? R.layout.row_app_home : R.layout.row_app, parent, false);
        return new AppViewHolder(itemView, viewType);

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
