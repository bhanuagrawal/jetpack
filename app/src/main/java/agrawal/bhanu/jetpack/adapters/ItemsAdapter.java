package agrawal.bhanu.jetpack.adapters;

import android.app.Application;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.recyclerview.extensions.DiffCallback;
import android.support.v7.recyclerview.extensions.ListAdapterConfig;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.pojo.NetworkState;
import agrawal.bhanu.jetpack.pojo.reddit.Data;
import agrawal.bhanu.jetpack.pojo.reddit.Post;

public class ItemsAdapter extends PagedListAdapter<Post, ItemsAdapter.ItemViewHolder> {

    Application mApplication;

    public ItemsAdapter(Application application) {
        super(Post.DIFF_CALLBACK);
        mApplication = application;
    }



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(mApplication.getApplicationContext()).inflate(R.layout.row_beer, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {

        Post post = getItem(i);
        if (post != null) {
            itemViewHolder.bindTo(post);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
        }

    }

    public void setNetworkState(NetworkState networkState) {
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView beerNameTV;
        public TextView beerAlcTV;
        public TextView beerABCTv;
        public ConstraintLayout parentLayout;

        public ItemViewHolder(View view) {
            super(view);
            beerNameTV = (TextView) view.findViewById(R.id.beer_name);
            beerABCTv = (TextView) view.findViewById(R.id.beer_abc);

            parentLayout = (ConstraintLayout) view.findViewById(R.id.parentlayout);
            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

        public void bindTo(Post post) {
            beerNameTV.setText(post.getData().getTitle());
            beerABCTv.setText(post.getData().getSelftext());

        }
    }
}


