package agrawal.bhanu.jetpack.reddit.ui;

import android.app.WallpaperManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.MyApp;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.databinding.FragmentItemListBinding;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.network.model.Status;
import agrawal.bhanu.jetpack.reddit.model.Post;
import dagger.hilt.android.AndroidEntryPoint;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ItemsList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ItemsList#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
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


    @Inject
    public ItemsAdapter itemsAdapter;

    @Inject
    WallpaperManager wallpaperManager;
    private FragmentItemListBinding binding;


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


        postViewModel = new ViewModelProvider(getActivity()).get(RedditPostViewModel.class);
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
                    binding.swiperefresh.setRefreshing(networkState == NetworkState.LOADING);
                    binding.itemlist.setVisibility(networkState.getStatus() == Status.FAILDED?View.GONE:View.VISIBLE);
                    binding.errorLayout.setVisibility(networkState.getStatus() == Status.FAILDED?View.VISIBLE:View.GONE);

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
        binding = FragmentItemListBinding.inflate(inflater, container, false);
        binding.itemlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.itemlist.setAdapter(itemsAdapter);
        binding.swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                postViewModel.onRefresh();
            }
        });
        binding.retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postViewModel.onRefresh();
            }
        });

/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                AppUtils.checkIfAlreadyhavePermission(getActivity().getApplication())) {
            itemRV.setBackground(wallpaperManager.getDrawable());
        }
*/

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
