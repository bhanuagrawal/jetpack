package agrawal.bhanu.jetpack.components;
import javax.inject.Singleton;

import agrawal.bhanu.jetpack.ItemKeyedPostDataSource;
import agrawal.bhanu.jetpack.PostDataSourceFactory;
import agrawal.bhanu.jetpack.PostRepository;
import agrawal.bhanu.jetpack.PostViewModel;
import agrawal.bhanu.jetpack.RedditPostViewModel;
import agrawal.bhanu.jetpack.fragments.ItemsList;
import agrawal.bhanu.jetpack.modules.AppModule;
import agrawal.bhanu.jetpack.modules.WebModule;
import agrawal.bhanu.jetpack.WebService;
import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, WebModule.class})
public interface WebComponent {
    void inject(PostViewModel postViewModel);
    void inject(WebService webService);
    void inject(PostRepository postRepository);
    void inject(RedditPostViewModel redditPostViewModel);
    void inject(PostDataSourceFactory postDataSourceFactory);
    void inject(ItemsList itemsList);
    void inject(ItemKeyedPostDataSource itemsList);
}
