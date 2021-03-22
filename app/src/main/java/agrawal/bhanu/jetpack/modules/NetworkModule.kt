package agrawal.bhanu.jetpack.modules

import agrawal.bhanu.jetpack.network.WebService
import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.data.PostRepository
import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter
import android.app.Application
import android.content.Context
import android.net.Uri
import com.android.volley.RequestQueue
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import javax.inject.Provider
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun providesPostRepository(gson: Gson, uriBuilder: Uri.Builder, webService: WebService): PostRepository {
        return PostRepository(gson, uriBuilder, webService)
    }

    @Provides
    @Singleton
    fun providesWebService( requestQueue: RequestQueue): WebService {
        return WebService(requestQueue)
    }

    @Provides
    @Singleton
    fun providePostDataSourceFactory(provider: Provider<ItemKeyedPostDataSource>): PostDataSourceFactory {
        return PostDataSourceFactory(provider)
    }

    @Provides
    @Singleton
    fun providesItemAdapter(application: Application?): ItemsAdapter {
        return ItemsAdapter(application)
    }

    @Provides
    fun providesItemKeyedPostDataSource(retryExecuter: Executor?, postRepository: PostRepository): ItemKeyedPostDataSource {
        return ItemKeyedPostDataSource(retryExecuter, postRepository)
    }

    @Provides
    @Singleton
    fun provideUrlBuilder(): Uri.Builder {
        return Uri.Builder()
    }
}