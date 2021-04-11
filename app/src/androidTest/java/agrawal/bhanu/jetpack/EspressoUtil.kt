package agrawal.bhanu.jetpack

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

object EspressoUtil {

    fun isRefreshing(): BoundedMatcher<View, SwipeRefreshLayout> {
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


    fun isPopulated(condition: (RecyclerView) -> Boolean): Matcher<in View>? {
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