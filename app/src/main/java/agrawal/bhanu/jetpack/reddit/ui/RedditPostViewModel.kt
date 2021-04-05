package agrawal.bhanu.jetpack.reddit.ui

import agrawal.bhanu.jetpack.network.model.NetworkState
import agrawal.bhanu.jetpack.reddit.data.ItemKeyedPostDataSource
import agrawal.bhanu.jetpack.reddit.data.PostDataSourceFactory
import agrawal.bhanu.jetpack.reddit.model.Post
import androidx.arch.core.util.Function
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RedditPostViewModel @Inject constructor(
        var postDataSourceFactory: PostDataSourceFactory
) : ViewModel() {

    var postList: LiveData<PagedList<Post>?>? = null
        get() {
            if (field == null) {
                val pagedListConfig = PagedList.Config.Builder().setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(20)
                        .setPageSize(20).build()
                field = LivePagedListBuilder(postDataSourceFactory, pagedListConfig)
                        .build()
            }
            return field
        }
        private set

    val networkState: LiveData<NetworkState?> = Transformations.switchMap(
            postDataSourceFactory.mutableLiveData
        ){
            it.networkState as LiveData<NetworkState?>
        }
    val initloading: LiveData<NetworkState?> = Transformations.switchMap(
            postDataSourceFactory.mutableLiveData
    ){
        it.initloading as LiveData<NetworkState?>
    }


    fun onRefresh() {
        postDataSourceFactory.mutableLiveData.value!!.invalidate()
    }

    fun retry() {
        postDataSourceFactory.mutableLiveData.value!!.retry()
    }
}