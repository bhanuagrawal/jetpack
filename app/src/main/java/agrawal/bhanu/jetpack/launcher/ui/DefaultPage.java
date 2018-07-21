package agrawal.bhanu.jetpack.launcher.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.ui.folder.AppsFolder;
import agrawal.bhanu.jetpack.launcher.ui.folder.Folder;
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
    private AppsViewModel mAppsModel;

    @BindView(R.id.default_apps)
    RecyclerView defaultApps;

    @BindView(R.id.layout)
    ConstraintLayout layout;

    @BindView(R.id.folderContainer)
    FrameLayout folderView;


    private AppsAdapter appsAdapter;
    private LinearLayoutManager layoutManager;
    private Folder folder;
    private AppsFolder appsFolder;

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
        mAppsModel = ViewModelProviders.of(getActivity()).get(AppsViewModel.class);
        appsAdapter = new AppsAdapter(getActivity(), new ArrayList<AppDTO>(), AppsAdapter.HOME);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        final Observer<AppsInfo> appsObserver = new Observer<AppsInfo>() {
            @Override
            public void onChanged(@Nullable final AppsInfo appsInfo) {
                folderView.setVisibility(mAppsModel.getAppsByFolderId(MainActivity.FREQUENT_APPS).isEmpty()?View.GONE:View.VISIBLE);
                appsAdapter.setApps(appsInfo.getDefaultApps());
            }
        };
        mAppsModel.getAppsInfo().observe(this, appsObserver);
        mAppsModel.getWallpaper().observe(this, new Observer<Drawable>() {
            @Override
            public void onChanged(@Nullable Drawable drawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    layout.setBackground(drawable);
                }
            }
        });
        appsFolder = Folder.newFolder(savedInstanceState, getContext(), R.id.folderContainer, "Frequent Apps", MainActivity.FREQUENT_APPS);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        ((FragmentActivity)getContext()).getSupportFragmentManager().putFragment(outState, MainActivity.FREQUENT_APPS, appsFolder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.default_page, container, false);
        ButterKnife.bind(this, view);
        defaultApps.setLayoutManager(layoutManager);
        defaultApps.setAdapter(appsAdapter);
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
