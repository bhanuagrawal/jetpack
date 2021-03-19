package agrawal.bhanu.jetpack.modules

import agrawal.bhanu.jetpack.launcher.data.AppsRepository
import agrawal.bhanu.jetpack.launcher.data.LauncherDatabase
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class LocalDataModule {
    @Provides
    @Singleton
    fun providesAppsRepository(@ApplicationContext appContext: Context, gson: Gson, database: LauncherDatabase): AppsRepository {
        return AppsRepository(appContext, gson, database)
    }

    @Provides
    @Singleton
    fun providesLauncherDatabase(@ApplicationContext appContext: Context): LauncherDatabase {
        return Room.databaseBuilder(appContext,
                LauncherDatabase::class.java, "launcher-data").build()
    }
}