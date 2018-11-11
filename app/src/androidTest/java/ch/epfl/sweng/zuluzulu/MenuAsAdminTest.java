package ch.epfl.sweng.zuluzulu;

import android.support.test.espresso.contrib.NavigationViewActions;

import org.junit.Before;
import org.junit.Test;

import ch.epfl.sweng.zuluzulu.TestingUtility.TestWithAdmin;
import ch.epfl.sweng.zuluzulu.TestingUtility.Utility;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class MenuAsAdminTest extends TestWithAdmin {

    @Before
    public void init() {
        Utility.openMenu();
    }

    @Test
    public void testCanOpenAssociationsGenerator() {
        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_associations_generator));

        Utility.checkFragmentIsOpen(R.id.associations_generator_fragment);
    }
}
