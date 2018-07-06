package agrawal.bhanu.jetpack.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.RedditPostViewModel;
import agrawal.bhanu.jetpack.adapters.ItemsAdapter;
import agrawal.bhanu.jetpack.model.NetworkState;
import agrawal.bhanu.jetpack.model.reddit.Post;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemsList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private RedditPostViewModel postViewModel;
    private int pageNo;
    private RecyclerView beerRV;
    @Inject
    ItemsAdapter itemsAdapter;
    private SwipeRefreshLayout swifeToRefresh;


    public ItemsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemsList.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemsList newInstance(int pageNo, String param2) {
        ItemsList fragment = new ItemsList();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pageNo);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageNo = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((MyApp)getActivity().getApplication()).getWebComponent().inject(this);

        postViewModel = ViewModelProviders.of(getActivity()).get(RedditPostViewModel.class);
        final Observer<PagedList<Post>> postObserver = new Observer<PagedList<Post>>() {
            @Override
            public void onChanged(@Nullable final PagedList<Post> pagedList) {
                itemsAdapter.submitList(pagedList);
            }
        };


        final Observer<NetworkState> networkObserver = new Observer<NetworkState>() {

            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                itemsAdapter.setNetworkState(networkState);
            }
        };

        postViewModel.getInitloading().observe(this, new Observer<NetworkState>() {
            @Override
            public void onChanged(@Nullable NetworkState networkState) {
                if(networkState != null){
                    swifeToRefresh.setRefreshing(networkState == NetworkState.LOADING);
                }

            }
        });


        postViewModel.getPostList().observe(this, postObserver);
        postViewModel.getNetworkState().observe(this, networkObserver);
        itemsAdapter.setRetryCallback(new ItemsAdapter.RetryCallback() {
            @Override
            public void retry() {
                postViewModel.retry();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beer_list, container, false);
        beerRV = (RecyclerView)view.findViewById(R.id.beerlist);
        beerRV.setLayoutManager(new LinearLayoutManager(getActivity()));
        beerRV.setAdapter(itemsAdapter);
        swifeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swiperefresh);
        swifeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postViewModel.onRefresh();
            }
        });
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
    }
}
