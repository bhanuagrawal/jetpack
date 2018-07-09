package agrawal.bhanu.jetpack.reddit.ui;

import android.app.Application;
import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.network.model.Status;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.reddit.model.Post;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemsAdapter extends PagedListAdapter<Post, RecyclerView.ViewHolder> {

    Application mApplication;
    private NetworkState networkState;
    private RetryCallback retryCallback;

    public ItemsAdapter(Application application) {
        super(Post.DIFF_CALLBACK);
        mApplication = application;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        if(viewType == R.layout.row_item){
            return new ItemViewHolder(layoutInflater.inflate(viewType, viewGroup, false));
        }
        else if(viewType == R.layout.network_state_item){
            return new NetworkStateViewHolder(layoutInflater.inflate(viewType, viewGroup, false));
        }
        else{
            return null;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        switch (getItemViewType(i)){
            case R.layout.row_item:
                Post post = getItem(i);
                if (post != null) {
                    ((ItemViewHolder)viewHolder).bindTo(post);
                } else {
                    // Null defines a placeholder item - PagedListAdapter automatically
                    // invalidates this row when the actual object is loaded from the
                    // database.
                }
                break;

            case R.layout.network_state_item:
                ((NetworkStateViewHolder)viewHolder).bindTo(networkState);
        }

    }

    public void setNetworkState(NetworkState networkState) {

/*        if(networkState == NetworkState.LOADED){
            notifyItemInserted(getItemCount());
        }*/
        NetworkState previousState = this.networkState;
        boolean previousExtraRow = hasExtraRow();
        this.networkState = networkState;
        boolean newExtraRow = hasExtraRow();
        if (previousExtraRow != newExtraRow) {
            if (previousExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (newExtraRow && previousState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }


    private boolean hasExtraRow() {
        if (networkState != null && networkState != NetworkState.LOADED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.network_state_item;
        } else {
            return R.layout.row_item;
        }
    }

    public void setRetryCallback(RetryCallback retryCallback) {
        this.retryCallback = retryCallback;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.title)
        public TextView titleTV;

        @BindView(R.id.free_text)
        public TextView freeTextTv;

        @BindView(R.id.subreddit)
        public TextView subredditTV;

        @BindView(R.id.upvotes)
        public TextView upvotesTv;


        @BindView(R.id.parentlayout)
        public ConstraintLayout parentLayout;

        @BindView(R.id.image)
        public ImageView imageView;

        public ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            parentLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
        }

        public void bindTo(final Post post) {
            titleTV.setText(post.getData().getTitle());
            freeTextTv.setText(post.getData().getSelftext());
            subredditTV.setText(post.getData().getSubreddit());
            upvotesTv.setText(String.valueOf(post.getData().getUps()) + " Upvodes");
            Picasso.get()
                    .load(post.getData().getThumbnail()) // thumbnail url goes here
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get()
                                    .load(post.getData().getUrl()) // image url goes here
                                    .placeholder(imageView.getDrawable())
                                    .into(imageView);
                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });
            //Picasso.get().load(post.getData().getUrl()).into(imageView);
        }

    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder{

        public TextView loadingTv;
        public TextView errorMsgTv;
        public TextView retryTv;
        public NetworkStateViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingTv = (TextView) itemView.findViewById(R.id.loading);
            errorMsgTv = (TextView) itemView.findViewById(R.id.error);
            retryTv = (TextView) itemView.findViewById(R.id.retry);
            retryTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retryCallback.retry();
                }
            });
        }

        public void bindTo(NetworkState networkState) {
            loadingTv.setVisibility(networkState.getStatus() == Status.LOADING? View.VISIBLE: View.GONE);
            errorMsgTv.setVisibility(networkState.getStatus() == Status.FAILDED? View.VISIBLE: View.GONE);
            retryTv.setVisibility(networkState.getStatus() == Status.FAILDED? View.VISIBLE: View.GONE);
        }
    }

    public interface RetryCallback{
        void retry();
    }
}


