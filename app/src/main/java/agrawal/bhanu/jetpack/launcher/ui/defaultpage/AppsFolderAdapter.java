package agrawal.bhanu.jetpack.launcher.ui.defaultpage;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetMetadata;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AppsFolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int APP_CONTAINER = 5;
    private static final int EMPTY = 6;
    private final Context context;
    private final LauncherViewModel mAppsModel;
    private final FolderManager folderMananger;
    private ArrayList<WidgetsMetaData> widgetsMetaData;
    public static final int FOLDER = 2;

    @Inject
    Executor executor;

    public void setAppsFolder(ArrayList<WidgetsMetaData> widgetsMetaData) {

        ArrayList<WidgetsMetaData> oldData = this.widgetsMetaData;
        this.widgetsMetaData = widgetsMetaData;
        notifyDataChanged(oldData, widgetsMetaData);
    }

    private void notifyDataChanged(ArrayList<WidgetsMetaData> oldData, ArrayList<WidgetsMetaData> newData) {


        if(oldData.size() != newData.size()){
            notifyDataSetChanged();
        }
        else{
            for(int i=0; i<oldData.size(); i++){

                String a = mAppsModel.getJSonString(WidgetMetadata.class, oldData.get(i));
                String b = mAppsModel.getJSonString(WidgetMetadata.class, newData.get(i));

                if(!a.equals(b)){
                    notifyItemChanged(i);
                }
            }
        }

    }



    public AppsFolderAdapter(Context context, ArrayList<WidgetsMetaData> widgetsMetaData) {
        this.context = context;
        this.widgetsMetaData = widgetsMetaData;
        mAppsModel = ViewModelProviders.of((AppCompatActivity)context).get(LauncherViewModel.class);
        folderMananger = new FolderManager(context);
        ((MyApp)((AppCompatActivity) context).getApplication()).getLocalDataComponent().inject(this);
    }



    public class FolderViewHolder extends RecyclerView.ViewHolder implements  View.OnLongClickListener {

        @BindView(R.id.folderContainer)
        FrameLayout frameLayout;

        private PopupWindow popupWindow;

        public FolderViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnLongClickListener(this);
        }

        public void bindTo(final WidgetsMetaData widgetsMetaData) {

            itemView.setVisibility(widgetsMetaData.getAppsCount() == 0?View.INVISIBLE:View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int newContainerId = View.generateViewId();
                frameLayout.setId(newContainerId);
                folderMananger.newFolder(frameLayout.getId(), widgetsMetaData.getFolderId());
            }
        }


        public void hide() {
            itemView.setVisibility(View.GONE);
        }

        @Override
        public boolean onLongClick(View view) {

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customView = layoutInflater.inflate(R.layout.popup_window,null);
            TextView remove = (TextView)customView.findViewById(R.id.remove);
            popupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.showAsDropDown(view);
            popupWindow.setFocusable(true);
            popupWindow.update();
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            if(widgetsMetaData.get(getAdapterPosition()).isRemovable()){
                remove.setVisibility(View.VISIBLE);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAppsModel.removeFromHome(getAdapterPosition());
                    }
                });
            }

            return true;
        }
    }


    @Override
    public int getItemViewType(int position) {

        if(widgetsMetaData.get(position).getType().equals(Constants.APP_CONTAINER)){
            return APP_CONTAINER;
        }
        if(widgetsMetaData.get(position).getType().equals(Constants.FOLDER)){
            return FOLDER;
        }
        else{
            return EMPTY;
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
                        .inflate(R.layout.row_folder, parent, false);
                return new FolderViewHolder(itemView);

        }




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        if(holder.getItemViewType() ==  APP_CONTAINER){

            final AppViewHolder viewHolder = (AppViewHolder)holder;

            mAppsModel.getAppByContainer(widgetsMetaData.get(position).getAppContainerId()).observe((AppCompatActivity) context, new androidx.lifecycle.Observer<App>() {
                @Override
                public void onChanged(@Nullable App app) {
                    viewHolder.itemView.setVisibility(app == null?View.GONE:View.VISIBLE);
                    if(app != null){
                        viewHolder.appNameTV.setText(app.getAppName());
                        viewHolder.appIconIV.setImageDrawable(mAppsModel.getAppIcon(app.getAppPackage()));
                        viewHolder.setApp(app);
                    }
                }
            });

        }
        else if(holder.getItemViewType() ==  FOLDER){
            final FolderViewHolder viewHolder = (FolderViewHolder)holder;
            viewHolder.bindTo(widgetsMetaData.get(position));
        }
        else{
            final FolderViewHolder viewHolder = (FolderViewHolder)holder;
            viewHolder.hide();
        }
    }



    @Override
    public int getItemCount() {
        return widgetsMetaData.size();
    }
}
