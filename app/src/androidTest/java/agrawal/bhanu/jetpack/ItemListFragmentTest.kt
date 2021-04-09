package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter
import agrawal.bhanu.jetpack.reddit.ui.ItemsList
import android.util.Log
import android.view.View
import android.widget.ExpandableListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
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
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class ItemListFragmentTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var mIdlingResource: SimpleIdlingResource


    @Before
    fun initialize(){
        hiltRule.inject()
        IdlingRegistry.getInstance().register(mIdlingResource)
    }


    @Test
    fun redditPostIsLoading(){
        val fragment = launchFragmentInHiltContainer<ItemsList>()
        onView(withId(R.id.itemlist)).check(
            matches(
                isPopulated{
                    (it.adapter as ItemsAdapter).itemCount >= 20
                }
            )
        )
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


    private fun isPopulated(condition: (RecyclerView) -> Boolean): Matcher<in View>? {
        return object : BoundedMatcher<View, RecyclerView>(
            RecyclerView::class.java) {

            override fun describeTo(description: Description) {
                description.appendText("is Populated")
            }

            override fun matchesSafely(view: RecyclerView): Boolean {
                return condition(view)
            }
        }
    }

}