package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.ui.RedditPostViewModel
import androidx.paging.PagedList
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject



@HiltAndroidTest
class RedditViewmodelUnitTest {

    lateinit var viewModel: RedditPostViewModel

    @Inject
    lateinit var postDataSourceFactory: PostDataSourceFactory


    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

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
            delay(5000)
            assert((list as PagedList<*>).isNotEmpty())
        }
    }
}