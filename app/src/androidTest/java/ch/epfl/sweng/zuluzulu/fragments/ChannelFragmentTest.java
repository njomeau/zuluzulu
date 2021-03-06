package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

@RunWith(AndroidJUnit4.class)
public class ChannelFragmentTest extends TestWithAuthenticatedAndFragment<ChannelFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());
        fragment = ChannelFragment.newInstance(user);
    }

    @Test
    public void testUserCanClickOnChannels() {
        onView(withId(R.id.channels_list_view)).check(matches(hasChildCount(1)));
        onData(anything()).inAdapterView(withId(R.id.channels_list_view))
                .atPosition(0)
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(R.id.chat_posts_buttons)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserCanSwipeUp() {
        onView(withId(R.id.channels_list_view)).perform(ViewActions.swipeDown());
    }
}
