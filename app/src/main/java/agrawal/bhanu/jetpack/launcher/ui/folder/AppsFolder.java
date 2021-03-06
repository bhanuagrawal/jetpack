package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.data.entities.Folder;
import agrawal.bhanu.jetpack.launcher.data.entities.FolderWidget;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppsFolder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppsFolder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppsFolder extends Fragment implements  LifecycleObserver{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String folderId;

    private OnFragmentInteractionListener mListener;
    private AppsFolderDialogFragmnet appsDialog;


    private RecyclerView appRV;
    private AppsAdapter appAppsAdapter;
    private GridLayoutManager layoutManager;

    @BindView(R.id.folderLayout)
    RelativeLayout folderLayout;

    @BindView(R.id.folderName)
    TextView folderNameTv;

    @Inject
    Executor executor;

    public FolderWidget folderWidget;

    private Unbinder uibinder;


    private int container;
    private LauncherViewModel mAppsModel;
    private PopupWindow popupWindow;

    public AppsFolder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Apps.
     */
    // TODO: Rename and change types and number of parameters
    public static AppsFolder newInstance(int container, String param1, String param2) {
        AppsFolder fragment = new AppsFolder();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM3, container);
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderId = getArguments().getString(ARG_PARAM2);
            container = getArguments().getInt(ARG_PARAM3);
        }

        layoutManager = new GridLayoutManager(getActivity(), 1);
        appAppsAdapter = new AppsAdapter(getActivity(), new ArrayList<App>(), AppsAdapter.FOLDER);
        mAppsModel = ViewModelProviders.of(getActivity()).get(LauncherViewModel.class);

        mAppsModel.getFolderById(folderId).observe(getActivity(), new Observer<FolderWidget>() {

            @Override
            public void onChanged(@Nullable final FolderWidget folderWidget) {
                if(folderWidget!=null){
                    folderNameTv.setText(folderWidget.getFolder().getFolderName());
                    if(folderWidget.getWidget().getRemovable()){
                        folderLayout.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View view) {
                                LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                View customView = layoutInflater.inflate(R.layout.popup_window,null);
                                TextView remove = (TextView)customView.findViewById(R.id.remove);
                                popupWindow = new PopupWindow(customView, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                                popupWindow.showAsDropDown(view);
                                popupWindow.setFocusable(true);
                                popupWindow.update();
                                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                remove.setVisibility(View.VISIBLE);
                                remove.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        popupWindow.dismiss();
                                        mAppsModel.removeFromHome(folderId);
                                    }
                                });
                                return false;
                            }
                        });
                    }
                    folderLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            appsDialog = AppsFolderDialogFragmnet.newInstance(folderId, folderWidget.getFolder().getFolderName());
                            appsDialog.show(((FragmentActivity)getContext()).getSupportFragmentManager(), MainActivity.APPS_DIALOG);
                        }
                    });
                }

            }
        });


        mAppsModel.getAppsByFolderId(folderId).observe(getActivity(), new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable List<App> apps) {
                appRV.setLayoutFrozen(false);
                layoutManager.setSpanCount(2);
                appRV.setLayoutManager(layoutManager);
                appAppsAdapter.setApps((ArrayList<App>) apps);
                appRV.setLayoutFrozen(true);
            }
        });


        ((MyApp)getActivity().getApplication()).getLocalDataComponent().inject(this);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.apps_folder, container, false);
        uibinder = ButterKnife.bind(this, view);
        appRV = (RecyclerView)view.findViewById(R.id.applist);
        appRV.setLayoutManager(layoutManager);
        appRV.setAdapter(appAppsAdapter);
        appRV.setLayoutFrozen(true);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uibinder.unbind();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void onFragmentCreated(Fragment fragment);
    }
}
