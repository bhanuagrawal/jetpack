package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.launcher.data.AppsRepository
import agrawal.bhanu.jetpack.launcher.model.AppsInfo
import agrawal.bhanu.jetpack.launcher.ui.LauncherViewModel
import android.app.WallpaperManager
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltAndroidTest
class LauncherViewModelTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @ApplicationContext
    lateinit var appContext: Context

    @Inject
    lateinit var wallpaperManager: WallpaperManager

    @Inject
    lateinit var appsRepository: AppsRepository

    @Inject
    lateinit var executor: Executor

    lateinit var launcherViewModel: LauncherViewModel


    @Before
    fun initialize(){
        hiltRule.inject()
        launcherViewModel = LauncherViewModel(
            appContext,
            wallpaperManager,
            appsRepository,
            executor
        )
    }

    @Test
    fun appsGettingFetched(){
        runBlocking(Dispatchers.Main) {
            val appsInfo = TestUtil.fetchLiveData(
                launcherViewModel.appsInfo,
                waitTime = 5000
            ) {
                it?.apps?.isNullOrEmpty() == false
            }
            assert((appsInfo as AppsInfo).apps.isNotEmpty())
        }
    }



}