package agrawal.bhanu.jetpack.launcher.ui.allapps;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.launcher.data.entities.App;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Apps.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Apps#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Apps extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RecyclerView appRV;
    private AppsAdapter appAppsAdapter;
    private LauncherViewModel mAppsModel;
    private int position;
    private GridLayoutManager layoutManager;

    public Apps() {
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
    public static Apps newInstance(int param1, String param2) {
        Apps fragment = new Apps();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mAppsModel = ViewModelProviders.of(getActivity()).get(LauncherViewModel.class);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        appAppsAdapter = new AppsAdapter(getActivity(), new ArrayList<App>(), AppsAdapter.ALL_APPS);
        final Observer<AppsInfo> appsObserver = new Observer<AppsInfo>() {
            @Override
            public void onChanged(@Nullable final AppsInfo appsInfo) {
                layoutManager.setSpanCount(mAppsModel.getColumn_count());
                appRV.setLayoutManager(layoutManager);
                appAppsAdapter.setApps(AppUtils.getApps(appsInfo, mAppsModel.getAppsCountPerPage(), position));
            }
        };
        mAppsModel.getAppsInfo().observe(this, appsObserver);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the rootLayout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps, container, false);
        appRV = (RecyclerView)view.findViewById(R.id.applist);
        appRV.setLayoutManager(layoutManager);
        registerForContextMenu(appRV);
        appRV.setAdapter(appAppsAdapter);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
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
