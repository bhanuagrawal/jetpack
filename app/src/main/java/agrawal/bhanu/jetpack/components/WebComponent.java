package agrawal.bhanu.jetpack.components;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory;
import agrawal.bhanu.jetpack.reddit.data.PostRepository;
import agrawal.bhanu.jetpack.reddit.ui.RedditPostViewModel;
import agrawal.bhanu.jetpack.reddit.ui.ItemsList;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.modules.NetworkModule;
import agrawal.bhanu.jetpack.network.WebService;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface WebComponent {
    void inject(WebService webService);
    void inject(PostRepository postRepository);
    void inject(RedditPostViewModel redditPostViewModel);
    void inject(PostDataSourceFactory postDataSourceFactory);
    void inject(ItemsList itemsList);
    void inject(ItemKeyedPostDataSource itemsList);
}
