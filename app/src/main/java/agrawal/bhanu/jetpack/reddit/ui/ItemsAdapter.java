package agrawal.bhanu.jetpack.reddit.ui;

import android.app.Application;
import androidx.paging.PagedListAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import agrawal.bhanu.jetpack.Constants;
import agrawal.bhanu.jetpack.R;
import agrawal.bhanu.jetpack.databinding.NetworkStateItemBinding;
import agrawal.bhanu.jetpack.databinding.RowItemBinding;
import agrawal.bhanu.jetpack.network.model.Status;
import agrawal.bhanu.jetpack.network.model.NetworkState;
import agrawal.bhanu.jetpack.reddit.model.Post;

public class ItemsAdapter extends PagedListAdapter<Post, RecyclerView.ViewHolder> {

    private final Context context;
    private NetworkState networkState;
    private RetryCallback retryCallback;

    public ItemsAdapter(Context context) {
        super(Post.DIFF_CALLBACK);
        this.context = context;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        if(viewType == R.layout.row_item){
            return new ItemViewHolder(RowItemBinding.inflate(layoutInflater, viewGroup, false));
        }
        else if(viewType == R.layout.network_state_item){
            return new NetworkStateViewHolder(NetworkStateItemBinding.inflate(layoutInflater, viewGroup, false));
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


        private final RowItemBinding binding;

        public ItemViewHolder(RowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.parentlayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.REDDIT_BASE_URL + getItem(position).getData().getPermalink()));
            browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(browserIntent);

        }

        public void bindTo(final Post post) {
            binding.title.setText(post.getData().getTitle());
            binding.freeText.setText(post.getData().getSelftext());
            binding.subreddit.setText(post.getData().getSubreddit());
            binding.upvotes.setText(String.valueOf(post.getData().getUps()) + " Upvotes");
            Picasso.get()
                    .load(post.getData().getThumbnail()) // thumbnail url goes here
                    .into(binding.image, new Callback() {
                        @Override
                        public void onSuccess() {
                            Picasso.get()
                                    .load(post.getData().getUrl()) // image url goes here
                                    .placeholder(binding.image.getDrawable())
                                    .fit()
                                    .into(binding.image);
                        }

                        @Override
                        public void onError(Exception e) {

                        }

                    });
            //Picasso.get().load(post.getData().getUrl()).into(imageView);
        }

    }

    class NetworkStateViewHolder extends RecyclerView.ViewHolder{

        private final NetworkStateItemBinding binding;

        public NetworkStateViewHolder(@NonNull NetworkStateItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    retryCallback.retry();
                }
            });
        }

        public void bindTo(NetworkState networkState) {
            binding.loading.setVisibility(networkState.getStatus() == Status.LOADING? View.VISIBLE: View.GONE);
            binding.error.setVisibility(networkState.getStatus() == Status.FAILDED? View.VISIBLE: View.GONE);
            binding.retry.setVisibility(networkState.getStatus() == Status.FAILDED? View.VISIBLE: View.GONE);
        }
    }

    public interface RetryCallback{
        void retry();
    }
}


