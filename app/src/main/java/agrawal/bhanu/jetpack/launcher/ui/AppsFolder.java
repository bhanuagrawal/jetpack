package agrawal.bhanu.jetpack.launcher.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.MainActivity;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
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
public class AppsFolder extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String folderName;
    private String folderId;

    private OnFragmentInteractionListener mListener;
    private RecyclerView appRV;
    private AppsAdapter appAppsAdapter;
    private AppsViewModel mAppsModel;
    private GridLayoutManager layoutManager;

    @BindView(R.id.folderLayout)
    RelativeLayout folderLayout;

    @BindView(R.id.folderName)
    TextView folderNameTv;


    private Unbinder uibinder;

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
    public static AppsFolder newInstance(String param1, String param2) {
        AppsFolder fragment = new AppsFolder();
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
            folderName = getArguments().getString(ARG_PARAM1);
            folderId = getArguments().getString(ARG_PARAM2);
        }

        mAppsModel = ViewModelProviders.of(getActivity()).get(AppsViewModel.class);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        appAppsAdapter = new AppsAdapter(getActivity(), new ArrayList<AppDTO>(), AppsAdapter.FOLDER);
        mAppsModel.getAppSuggestions().observe(this, new Observer<ArrayList<AppDTO>>() {
            @Override
            public void onChanged(@Nullable ArrayList<AppDTO> apps) {
                layoutManager.setSpanCount(2);
                appRV.setLayoutManager(layoutManager);
                appAppsAdapter.setApps(apps);
            }
        });
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
        folderLayout.setOnClickListener(this);
        folderNameTv.setText(folderName);
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


    @Override
    public void onClick(View view) {
        AppsFolderDialogFragmnet appsDialog = AppsFolderDialogFragmnet.newInstance("", "");
        appsDialog.show(getChildFragmentManager(), MainActivity.APPS_DIALOG);
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
