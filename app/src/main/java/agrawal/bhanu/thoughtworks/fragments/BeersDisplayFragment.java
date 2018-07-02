package agrawal.bhanu.thoughtworks.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import agrawal.bhanu.thoughtworks.AppUtils;
import agrawal.bhanu.thoughtworks.pojo.BeerPOJO;
import agrawal.bhanu.thoughtworks.BeerViewModel;
import agrawal.bhanu.thoughtworks.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BeersDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BeersDisplayFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private MyAdapter mAdapter;
    private ViewPager beerAdapter;
    private TabLayout tabLayout;
    private BeerViewModel mBeerModel;

    public BeersDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BeersDisplayFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BeersDisplayFragment newInstance(String param1, String param2) {
        BeersDisplayFragment fragment = new BeersDisplayFragment();
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
        mBeerModel = ViewModelProviders.of(getActivity()).get(BeerViewModel.class);
        final Observer<ArrayList<BeerPOJO>> appsObserver = new Observer<ArrayList<BeerPOJO>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<BeerPOJO> beers) {
                mAdapter.setNUM_ITEMS(AppUtils.getNoOfPages(beers));
            }
        };

        mBeerModel.getBeersList().observe(this, appsObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_beer_display, container, false);
        beerAdapter = (ViewPager) rootView.findViewById(R.id.all_beers_viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
        beerAdapter.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(beerAdapter);
        return rootView;
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
            return BeerList.newInstance(position, "");
        }
    }
}
