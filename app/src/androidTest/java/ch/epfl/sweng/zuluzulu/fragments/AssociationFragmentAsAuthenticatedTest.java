package ch.epfl.sweng.zuluzulu.fragments;

import android.support.test.espresso.action.ViewActions;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAuthenticatedAndFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class AssociationFragmentAsAuthenticatedTest extends TestWithAuthenticatedAndFragment<AssociationFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = AssociationFragment.newInstance(user);
    }

    @Test
    public void authenticatedCanClickOnFavorites() {
        onView(withText(R.string.fragment_assos_button_fav)).perform(ViewActions.click());
        onView(withText(R.string.fragment_assos_button_all)).perform(ViewActions.click());

        /*
        onView(withId(R.id.association_fragment_listview))
                .check(matches(hasChildCount(1)));
        */
    }
}
