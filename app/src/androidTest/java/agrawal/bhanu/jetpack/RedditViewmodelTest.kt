package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.model.Post
import agrawal.bhanu.jetpack.reddit.ui.RedditPostViewModel
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagedList
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class RedditViewmodelTest {

    lateinit var viewModel: RedditPostViewModel

    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    lateinit var postDataSourceFactory: PostDataSourceFactory


    @get:Rule
    val rule = RuleChain
            .outerRule(hiltRule)
            .around(instantTaskExecutorRule)

    @Before
    fun initialize(){
        hiltRule.inject()
        viewModel = RedditPostViewModel(postDataSourceFactory)
    }


    @Test
    fun assertPostFetchedFromNetwork(){

        runBlocking{
            val list = TestUtil.fetchLiveData(viewModel.postList!!){
                it.isNullOrEmpty().not()
            }

            assert(list.isNullOrEmpty().not())
        }
    }

    @Test
    fun assertPostFetchedFromNetwork1(){
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