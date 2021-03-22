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
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.databinding.RowAppContainerBinding;
import agrawal.bhanu.jetpack.databinding.RowFolderBinding;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetMetadata;
import agrawal.bhanu.jetpack.launcher.data.entities.WidgetsMetaData;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import agrawal.bhanu.jetpack.launcher.ui.viewholder.AppViewHolder;

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
    }



    public class FolderViewHolder extends RecyclerView.ViewHolder implements  View.OnLongClickListener {


        private final RowFolderBinding binding;
        private PopupWindow popupWindow;

        public FolderViewHolder(RowFolderBinding  binding ){
            super(binding.getRoot());
            this.binding = binding;
            itemView.setOnLongClickListener(this);
        }

        public void bindTo(final WidgetsMetaData widgetsMetaData) {

            itemView.setVisibility(widgetsMetaData.getAppsCount() == 0?View.INVISIBLE:View.VISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                int newContainerId = View.generateViewId();
                binding.folderContainer.setId(newContainerId);
                folderMananger.newFolder(binding.folderContainer.getId(), widgetsMetaData.getFolderId());
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


        switch (viewType){

            case APP_CONTAINER:
                return new AppViewHolder(RowAppContainerBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false), viewType, context);
            case FOLDER:
                return new FolderViewHolder(RowFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

            default:
                return new FolderViewHolder(RowFolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
                        ((TextView)viewHolder.itemView.findViewById(R.id.app_name)).setText(app.getAppName());
                        ((ImageView)viewHolder.itemView.findViewById(R.id.app_icon)).setImageDrawable(mAppsModel.getAppIcon(app.getAppPackage()));
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
