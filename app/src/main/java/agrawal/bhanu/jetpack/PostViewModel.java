package agrawal.bhanu.jetpack;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import agrawal.bhanu.jetpack.model.reddit.RedditFeed;

public class PostViewModel extends AndroidViewModel {

    private MutableLiveData<RedditFeed> redditFeed;
    @Inject
    PostRepository postRepository;

    public PostViewModel(@NonNull Application application) {
        super(application);
        ((MyApp)application).getWebComponent().inject(this);
    }

    public MutableLiveData<RedditFeed> getPosts() {

        if (redditFeed == null) {
            redditFeed = new MutableLiveData<RedditFeed>();
            new Thread(new Runnable() {
                @Override
                public void run() {
                   // postRepository.fetchPosts(redditFeed);
                }
            }).start();
        }
        return redditFeed;
    }
}
