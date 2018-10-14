package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasChildCount;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest {

    private final static int NB_ALL_ASSOS = 7;
    private final static int NB_FAV_ASSOS = 4;
    private AssociationFragment fragment;
    private ListView list_assos;

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    private void openDrawer(){
        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT))) // Left Drawer should be closed.
                .perform(DrawerActions.open());
    }

    private void waitFor(int millis) {
        try{
            Thread.sleep(millis);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    @Before
    public void goToAssociationList(){
        // Authenticate
        openDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_login));

        onView(withId(R.id.username)).perform(typeText("nicolas")).perform(closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("password")).perform(closeSoftKeyboard());
        onView(withId(R.id.sign_in_button)).perform(click());

        // Go to association view
        openDrawer();
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations));
        waitFor(5000);
        fragment = (AssociationFragment)mActivityRule.getActivity().getCurrentFragment();
        list_assos = fragment.getListviewAssos();
    }

    @Test
    public void mainPageHasSomeAssociations(){
        onView(withId(R.id.association_fragment_all_button)).perform(ViewActions.click());
        assertThat(list_assos, hasChildCount(NB_ALL_ASSOS));
    }

    @Test
    public void clickOnFavoritesDisplayFewerAssociations() {
        onView(withId(R.id.association_fragment_fav_button)).perform(ViewActions.click());
        assertThat(list_assos, hasChildCount(NB_FAV_ASSOS));
    }

    @Test
    public void clickingAnAssociationGoesToDetail() {
        onView(withText("Agepoly")).perform(ViewActions.click());
        onView(withId(R.id.association_detail_name)).check(matches(isDisplayed()));
    }


}
