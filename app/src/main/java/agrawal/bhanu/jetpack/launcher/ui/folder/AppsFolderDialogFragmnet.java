package agrawal.bhanu.jetpack.launcher.ui.folder;

import android.app.Dialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.model.AppDTO;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.launcher.ui.AppsAdapter;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppsFolderDialogFragmnet.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppsFolderDialogFragmnet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppsFolderDialogFragmnet extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String folderId;
    private String folderName;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.applist)
    RecyclerView appRV;

    @BindView(R.id.folder_title)
    TextView folderTitleTv;

    private AppsAdapter appAppsAdapter;
    private GridLayoutManager layoutManager;
    private LauncherViewModel mAppsModel;
    private Unbinder uibinder;


    public AppsFolderDialogFragmnet() {
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
    public static AppsFolderDialogFragmnet newInstance(String param1, String param2) {
        AppsFolderDialogFragmnet fragment = new AppsFolderDialogFragmnet();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            folderId = getArguments().getString(ARG_PARAM1);
            folderName = getArguments().getString(ARG_PARAM2);
        }
        appAppsAdapter = new AppsAdapter(getActivity(), new ArrayList<AppDTO>(), AppsAdapter.FOLDER_DIALOG);
        mAppsModel = ViewModelProviders.of(getActivity()).get(LauncherViewModel.class);

        mAppsModel.getAppsInfo().observe(this, new Observer<AppsInfo>() {
            @Override
            public void onChanged(@Nullable AppsInfo appsInfo) {
                appAppsAdapter.setApps(mAppsModel.getAppsByFolderId(folderId));
            }
        });
    }


    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        // handles https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        super.onDestroyView();
        uibinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.dialog_fragment_apps, container, false);
        uibinder = ButterKnife.bind(this, view);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        folderTitleTv.setText(folderName);
        appRV.setAdapter(appAppsAdapter);
        layoutManager = new GridLayoutManager(getActivity(), 1);
        layoutManager.setSpanCount(3);
        appRV.setLayoutManager(layoutManager);
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
