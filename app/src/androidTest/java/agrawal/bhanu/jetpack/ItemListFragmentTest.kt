package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter
import agrawal.bhanu.jetpack.reddit.ui.ItemsList
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Description
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
class ItemListFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)


    @Before
    fun initialize(){
        hiltRule.inject()
    }


    @Test
    fun redditPostIsLoading(){
        val fragment = launchFragmentInHiltContainer<ItemsList>()

        onView(withId(R.id.swiperefresh)).check(matches(isRefreshing()))
        Thread.sleep(5000)
        onView(withId(R.id.swiperefresh)).check(matches(not(isRefreshing())))


        val recyclerView: RecyclerView =
            fragment.activity!!.findViewById(R.id.itemlist)
        val itemCount = recyclerView.adapter!!.itemCount
        Log.d("test1", "itemcount: $itemCount")
        assert(itemCount >= 20)
//        onView(withId(R.id.itemlist))
//            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(itemCount - 1))

//        val adapter = recyclerView.adapter!! as ItemsAdapter
//        var lastItemUpvotes = Int.MAX_VALUE
//
//        for(item in 0 until itemCount){
//            Log.d(" test1 ", " ${lastItemUpvotes}, ${adapter.currentList?.get(item)?.data?.ups}")
//            if((adapter.currentList?.get(item)?.data?.ups!! - lastItemUpvotes) > 2000){
//                assert(false)
//            }
//            lastItemUpvotes = adapter.currentList?.get(item)?.data?.ups!!
//        }

    }

    @Test
    fun redditPostIsRefreshing() = runBlocking{
        val fragment = launchFragmentInHiltContainer<ItemsList>()
        val recyclerView: RecyclerView =
            fragment.activity!!.findViewById(R.id.itemlist)
        val adapter = recyclerView.adapter!! as ItemsAdapter

        withContext(Dispatchers.Default) { delay(5000) }

        val oldData = adapter.currentList?.get(0)?.data?.ups!!

        onView(withId(R.id.container))
            .perform(swipeDown())

        onView(withId(R.id.swiperefresh)).check(matches(isRefreshing()))
        withContext(Dispatchers.Default) { delay(5000) }
        onView(withId(R.id.swiperefresh)).check(matches(not(isRefreshing())))

        val newData = adapter.currentList?.get(0)?.data?.ups!!

        assert(newData != oldData)

    }


    private fun isRefreshing(): BoundedMatcher<View, SwipeRefreshLayout> {
        return object : BoundedMatcher<View, SwipeRefreshLayout>(
            SwipeRefreshLayout::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("is refreshing")
            }

            override fun matchesSafely(view: SwipeRefreshLayout): Boolean {
                return view.isRefreshing
            }
        }
    }


}