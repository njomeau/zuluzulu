package ch.epfl.sweng.zuluzulu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;

import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;

/**
 * This class allow us to create the main activity as if we were connected.
 * It creates an user and allow child to use this User.
 * <p>
 * Author @dahn
 */
public abstract class TestWithAuthenticatedUser {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class, false, false);


    private AuthenticatedUser user;

    @Before
    public void setUpLogin() {
        this.user = Utility.createTestAuthenticated();
        Utility.addUserToMainIntent(mActivityRule, this.user);
    }

    /**
     * Return the user for the childs
     *
     * @return User
     */
    protected AuthenticatedUser getUser() {
        return this.user;
    }
}
