package agrawal.bhanu.jetpack.launcher.ui.defaultpage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsAndFolder;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import agrawal.bhanu.jetpack.launcher.ui.folder.FolderManager;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DefaultPage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DefaultPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultPage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LauncherViewModel mAppsModel;

    @BindView(R.id.default_apps)
    RecyclerView defaultApps;

    @BindView(R.id.layout)
    ConstraintLayout rootLayout;

    @BindView(R.id.apps_folder_recyclerview)
    RecyclerView appsFolderRecyclerview;

    @Inject
    Executor executor;

    private AppsAdapter appsAdapter;
    private LinearLayoutManager layoutManager;

    private FolderManager folderManager;
    private GridLayoutManager appsFolderLayoutManager;
    private AppsFolderAdapter appsFolderAdapter;
    private ItemTouchHelper ith;

    public DefaultPage() {
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
    public static DefaultPage newInstance(String param1, String param2) {
        DefaultPage fragment = new DefaultPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((MyApp)getActivity().getApplication()).getLocalDataComponent().inject(this);
        mAppsModel = ViewModelProviders.of(getActivity()).get(LauncherViewModel.class);
        appsAdapter = new AppsAdapter(getActivity(), new ArrayList<AppDTO>(), AppsAdapter.HOME);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        appsFolderLayoutManager  =new GridLayoutManager(getActivity(), 1);
        appsFolderAdapter = new AppsFolderAdapter(getActivity(), new ArrayList<AppsAndFolder>());
        final Observer<AppsInfo> appsObserver = new Observer<AppsInfo>() {
            @Override
            public void onChanged(@Nullable final AppsInfo appsInfo) {;
                appsAdapter.setApps(appsInfo.getDefaultApps());
                appsFolderAdapter.notifyDataSetChanged();
            }
        };
        mAppsModel.getAppsInfo().observe(this, appsObserver);
        mAppsModel.getWallpaper().observe(this, new Observer<Drawable>() {
            @Override
            public void onChanged(@Nullable Drawable drawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootLayout.setBackground(drawable);
                }
            }
        });

        folderManager = new FolderManager(getContext());
        mAppsModel.getFolders().observe(this, new Observer<ArrayList<AppsAndFolder>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AppsAndFolder> appsFolder) {
                appsFolderLayoutManager.setSpanCount(4);
                appsFolderAdapter.setAppsFolder(appsFolder);
            }
        });

        ItemTouchHelper.Callback _ithCallback = new ItemTouchHelper.Callback() {
            //and in your imlpementaion of
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // get the viewHolder's and target's positions in your adapter data, swap them
                Collections.swap(mAppsModel.getFolders().getValue(), viewHolder.getAdapterPosition(), target.getAdapterPosition());
                // and notify the adapter that its dataset has changed
                appsFolderAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                mAppsModel.onFoldersChange();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //TODO
            }

            //defines the enabled move directions in each state (idle, swiping, dragging).
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,
                        ItemTouchHelper.DOWN | ItemTouchHelper.UP | ItemTouchHelper.START | ItemTouchHelper.END);
            }
        };

        ith = new ItemTouchHelper(_ithCallback);
//
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mAppsModel.updateDefaultPage(mAppsModel.getAppsInfo().getValue().getGoogleApp(), 5);
//            }
//        }, 2000);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the rootLayout for this fragment
        final View view = inflater.inflate(R.layout.default_page, container, false);
        ButterKnife.bind(this, view);
        defaultApps.setLayoutManager(layoutManager);
        defaultApps.setAdapter(appsAdapter);

        appsFolderRecyclerview.setLayoutManager(appsFolderLayoutManager);
        appsFolderRecyclerview.setAdapter(appsFolderAdapter);
        ith.attachToRecyclerView(appsFolderRecyclerview);

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
