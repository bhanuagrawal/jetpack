package agrawal.bhanu.jetpack.launcher.ui.viewholder;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.AppsFolderAdapter;
import agrawal.bhanu.jetpack.launcher.util.callbacks.Callback;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
        View.OnLongClickListener {

    private final Context context;
    private final LauncherViewModel mAppsModel;
    public TextView appNameTV;
    public ImageView appIconIV;
    public ConstraintLayout parentLayout;
    private App app;
    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
    private PackageManager pm;
    public PopupWindow popupWindow;

    @BindView(R.id.addToHome)
    TextView addToHome;

    @BindView(R.id.remove)
    TextView remove;

    @BindView(R.id.infoUninstall)
    TextView infoUninstall;

    @Inject
    Executor executor;

    public AppViewHolder(View view, int viewType, Context context) {
        super(view);
        if(viewType != AppsAdapter.FOLDER){
            view.setOnLongClickListener(this);
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
        ((MyApp)((FragmentActivity) context).getApplication()).getLocalDataComponent().inject(this);
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
            e.printStackTrace();
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }



    public void setApp(App app) {
        this.app = app;
    }

    @Override
    public boolean onLongClick(View view) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_window,null);
        ButterKnife.bind(this, customView);
        popupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.showAsDropDown(appIconIV);
        popupWindow.setFocusable(true);
        popupWindow.update();
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if(getItemViewType() == AppsAdapter.ALL_APPS){
            addToHome.setVisibility(View.VISIBLE);
            addToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mAppsModel.addToHome(app, new Callback() {
                        @Override
                        public void onSuccess() {
                            ((FragmentActivity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((FragmentActivity)context).onBackPressed();
                                }
                            });

                        }

                        @Override
                        public void onError(final String message) {
                            ((FragmentActivity)context).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });

                }
            });
        }


        if(getItemViewType() == AppsFolderAdapter.APP_CONTAINER){

            remove.setVisibility(View.VISIBLE);
            remove.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mAppsModel.removeFromHome(getAdapterPosition());
                }
            });
        }

        infoUninstall.setVisibility(View.VISIBLE);
        infoUninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                intent.setData(Uri.parse("package:" + app.getAppPackage()));
                context.startActivity(intent);
            }
        });

        return true;
    }
}