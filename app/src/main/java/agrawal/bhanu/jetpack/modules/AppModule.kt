package agrawal.bhanu.jetpack.modules

import agrawal.bhanu.jetpack.SimpleIdlingResource
import android.app.Application
import android.app.WallpaperManager
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.annotation.NonNull
import androidx.annotation.VisibleForTesting
import androidx.test.espresso.IdlingResource
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    @Singleton
    fun provideUrlBuilder(): Uri.Builder {
        return Uri.Builder()
    }

    @Provides
    @Singleton
    fun getWallpaperManager(@ApplicationContext appContext: Context): WallpaperManager {
        return WallpaperManager.getInstance(appContext)
    }

    @Provides
    @Singleton
    fun providesGson() = Gson()

    @Provides
    @Singleton
    fun providesExecuter(): Executor {
        return Executors.newFixedThreadPool(5)
    }

    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences("launcher", Context.MODE_PRIVATE)
    }


    /**
     * Only called from test, creates and returns a new [SimpleIdlingResource].
     */
    @Provides
    @Singleton
    fun provideIdlingResource(): SimpleIdlingResource = SimpleIdlingResource()
}