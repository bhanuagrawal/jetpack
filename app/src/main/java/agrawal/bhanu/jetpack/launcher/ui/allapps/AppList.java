package agrawal.bhanu.jetpack.launcher.ui.allapps;

import android.app.WallpaperManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.AppUtils;
import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.databinding.FragmentAppListBinding;
import agrawal.bhanu.jetpack.launcher.model.AppsInfo;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppList#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class AppList extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyAdapter mAdapter;
    private LauncherViewModel mAppsModel;


    @Inject
    WallpaperManager wallpaperManager;
    private FragmentAppListBinding binding;

    public AppList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppList.
     */
    // TODO: Rename and change types and number of parameters
    public static AppList newInstance(String param1, String param2) {
        AppList fragment = new AppList();
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


        mAdapter = new MyAdapter(getChildFragmentManager());
        mAppsModel = ViewModelProviders.of(getActivity()).get(LauncherViewModel.class);
        final Observer<AppsInfo> appsObserver = new Observer<AppsInfo>() {
            @Override
            public void onChanged(@Nullable final AppsInfo appsInfo) {
                mAdapter.setNUM_ITEMS(AppUtils.getNoOfPages(appsInfo, mAppsModel.getAppsPerPage()));
            }
        };
        mAppsModel.getAppsInfo().observe(this, appsObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAppListBinding.inflate(inflater, container, false);
        binding.allApppsViewpager.setAdapter(mAdapter);
        binding.tabDots.setupWithViewPager(binding.allApppsViewpager);
        return binding.getRoot();
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

    public static class MyAdapter extends FragmentStatePagerAdapter {
        private int NUM_ITEMS;

        public void setNUM_ITEMS(int NUM_ITEMS) {
            this.NUM_ITEMS = NUM_ITEMS;
            notifyDataSetChanged();
        }

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return Apps.newInstance(position, "");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
    }
}
