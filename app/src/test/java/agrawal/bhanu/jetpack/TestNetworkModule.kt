package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.modules.NetworkModule
import agrawal.bhanu.jetpack.network.WebService
import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.data.PostRepository
import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter
import android.app.Application
import android.content.Context
import android.net.Uri
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import org.mockito.Mockito.mock
import java.util.concurrent.Executor
import javax.inject.Provider
import javax.inject.Singleton

@TestInstallIn(components = [SingletonComponent::class],
    replaces = [NetworkModule::class]
)
@Module
class TestNetworkModule {

    @Provides
    @Singleton
    fun providesRequestQueue(@ApplicationContext appContext: Context): RequestQueue {
        return mock(RequestQueue::class.java)
    }
}