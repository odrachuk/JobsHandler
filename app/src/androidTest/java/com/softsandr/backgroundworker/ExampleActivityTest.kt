package com.softsandr.backgroundworker

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit


/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class ExampleActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule<JobsActivity>(JobsActivity::class.java)

    @Test
    fun emptyIsVisibleOnStart() {
        onView(withId(R.id.activity_jobs__empty)).check(ViewAssertions.matches((isDisplayed())))
    }

    @Test
    fun recyclerIsOnlyVisibleViewAfterStart() {
        onView(withId(R.id.activity_jobs__start_btn)).perform(click())

        TestUtils.waitFor(object : TestUtils.TestCondition("No items on page") {
            override fun conditionMet(): Boolean {
                return activityTestRule.activity.findViewById<View>(R.id.recycler_item__job_id).visibility == View.VISIBLE
            }
        }, TimeUnit.SECONDS.toMillis(5))

        onView(withId(R.id.activity_jobs__empty)).check(ViewAssertions.matches(not((isDisplayed()))))
    }
}
