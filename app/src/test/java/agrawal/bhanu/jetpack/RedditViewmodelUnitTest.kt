package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.modules.NetworkModule
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.ui.RedditPostViewModel
import android.content.Context
import android.net.http.RequestQueue
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.*
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject



@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class RedditViewmodelUnitTest {

    @Inject
    lateinit var postDataSourceFactory: PostDataSourceFactory

    lateinit var viewModel: RedditPostViewModel

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val instantTaskExecutorRule = InstantTaskExecutorRule()

//    @BindValue
//    @JvmField val mRequestQueue: RequestQueue = mock(RequestQueue::class.java)


    @Before
    fun initialize(){
        hiltRule.inject()
        viewModel = RedditPostViewModel(postDataSourceFactory)
    }


    @Test
    fun assertPostFetchedFromNetworkUnitTest(){

        runBlocking{
            var list: Any? = null
            viewModel.postList?.observeForever {
                list = it
            }
            delay(20000)
            assert((list as PagedList<*>).isNotEmpty())
        }
    }
}