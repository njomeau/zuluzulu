package ch.epfl.sweng.zuluzulu;

import android.media.SoundPool;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Structure.User;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.hasContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest {
    private static final String FAV_CONTENT = "This association is in your favorites";
    private static final String NOT_FAV_CONTENT = "This association isn't in your favorites";

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);

    @Before
    public void initAuthenticatedTest(){
        User user = Utility.createTestUser();
        Utility.addUserToMainIntent(mActivityRule, user);
        AssociationFragment fragment = AssociationFragment.newInstance(user);
        mActivityRule.getActivity().openFragment(fragment);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void authenticatedClickOnFavorites() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        onView(withId(R.id.association_fragment_listview)).check(matches(hasChildCount(Utility.TEST_FAV_ASSOCIATIONS)));
    }

    @Test
    public void authenticatedStayOnAll() {
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        onView(withId(R.id.association_fragment_listview)).check(matches(hasChildCount(Utility.NUMBER_OF_ASSOCIATIONS)));
    }

    @Test
    public void clickOnAlreadyFavAssociation(){
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withContentDescription(FAV_CONTENT)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnNotFavAssociation(){
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        onView(withText("Club Montagne")).perform(ViewActions.click());
        onView(withContentDescription(NOT_FAV_CONTENT)).check(matches(isDisplayed()));
    }


}
