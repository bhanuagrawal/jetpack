package agrawal.bhanu.jetpack.launcher.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.launcher.ui.allapps.AppList;
import agrawal.bhanu.jetpack.launcher.ui.defaultpage.DefaultPage;
import agrawal.bhanu.jetpack.reddit.ui.ItemsList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements ViewPager.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private Unbinder uibinder;

    @BindView(R.id.home_viewpager)
    ViewPager homeViewPager;
    private HomeViewPagerAdapter adapter;
    private int pagerNo;
    private int currentPage = Constants.DEFAULT_PAGE_POSITION;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        adapter = new HomeViewPagerAdapter(getChildFragmentManager());
        adapter.setNUM_ITEMS(3);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the rootLayout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        uibinder = ButterKnife.bind(this, view);
        homeViewPager.setAdapter(adapter);
        if(savedInstanceState != null &&
                savedInstanceState.containsKey("currentPage")){

            Log.d("currentPageReceived", String.valueOf(savedInstanceState.getInt("currentPage")));
            currentPage = savedInstanceState.getInt("currentPage");
        }
        homeViewPager.setCurrentItem(currentPage);
        homeViewPager.addOnPageChangeListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("currentPage", String.valueOf(homeViewPager.getCurrentItem()));
        outState.putInt("currentPage", homeViewPager.getCurrentItem());
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
    public void onPageScrolled(int i, float v, int i1) {
        mListener.onFragmentCreated(adapter.getItem(i));
    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void onBackPressed() {
        homeViewPager.setCurrentItem(Constants.DEFAULT_PAGE_POSITION, true);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uibinder.unbind();
    }

    public static class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
        private int NUM_ITEMS;

        public void setNUM_ITEMS(int NUM_ITEMS) {
            this.NUM_ITEMS = NUM_ITEMS;
            notifyDataSetChanged();
        }

        public HomeViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return ItemsList.newInstance(0, "");
                case 1:
                    return DefaultPage.newInstance("", "");
                case 2:
                    return AppList.newInstance("", "");
                default:
                    return ItemsList.newInstance(0, "");

            }
        }
    }
}
