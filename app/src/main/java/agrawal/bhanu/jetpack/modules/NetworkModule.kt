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
    fun provideUrlBuilder(): Uri.Builder {
        return Uri.Builder()
    }
}