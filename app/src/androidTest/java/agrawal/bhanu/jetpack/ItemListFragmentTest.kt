package agrawal.bhanu.jetpack

import agrawal.bhanu.jetpack.EspressoUtil.isPopulated
import agrawal.bhanu.jetpack.EspressoUtil.isRefreshing
import agrawal.bhanu.jetpack.reddit.ui.ItemsAdapter
import agrawal.bhanu.jetpack.reddit.ui.ItemsList
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
    fun redditPostIsRefreshing(){
        val fragment = launchFragmentInHiltContainer<ItemsList>()
        onView(withId(R.id.container)).check(matches(isDisplayed()))
        val recyclerView: RecyclerView =
            fragment.activity!!.findViewById(R.id.itemlist)
        val adapter = recyclerView.adapter!! as ItemsAdapter
        val oldData = adapter.currentList?.get(0)?.data?.ups!!
        onView(withId(R.id.container))
            .perform(swipeDown())
//        onView(withId(R.id.swiperefresh))
//            .check(matches(isRefreshing()))




//        onView(withId(R.id.swiperefresh))
//            .check(matches(not(isRefreshing())))


        onView(withId(R.id.itemlist)).check(
            matches(
                isPopulated{
                    val newData = adapter.currentList?.get(0)?.data?.ups!!
                    newData != oldData
                }
            )
        )

    }

}