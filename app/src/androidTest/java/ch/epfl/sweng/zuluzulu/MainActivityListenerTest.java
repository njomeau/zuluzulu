package ch.epfl.sweng.zuluzulu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.zuluzulu.database.MockedProxy;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.testingUtility.TestWithAdminAndFragment;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ABOUT_US_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_ASSOCIATION_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHANNEL_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHAT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_EVENT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_MAIN_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_PROFILE_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_SETTINGS_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.SET_TITLE;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MainActivityListenerTest extends TestWithAdminAndFragment<MainFragment> {

    @Override
    public void initFragment() {
        DatabaseFactory.setDependency(new MockedProxy());

        fragment = MainFragment.newInstance(user);
    }

    @Test
    public void communicationTest() throws Throwable {
        mActivityRule.runOnUiThread(() -> {
            // Utils to modify the main activity
            getMainActivity().onFragmentInteraction(SET_TITLE, "new_title");
            assertEquals("new_title", getMainActivity().getTitle());

            getMainActivity().onFragmentInteraction(OPEN_MAIN_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof MainFragment);

            getMainActivity().onFragmentInteraction(OPEN_ABOUT_US_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof AboutZuluzuluFragment);

            getMainActivity().onFragmentInteraction(OPEN_SETTINGS_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof SettingsFragment);

            getMainActivity().onFragmentInteraction(OPEN_PROFILE_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof ProfileFragment);

            getMainActivity().onFragmentInteraction(OPEN_CHAT_FRAGMENT, Utility.defaultChannel());
            assertTrue(getMainActivity().getCurrentFragment() instanceof ChatFragment);

            getMainActivity().onFragmentInteraction(OPEN_POST_FRAGMENT, Utility.defaultChannel());
            assertTrue(getMainActivity().getCurrentFragment() instanceof PostFragment);

            getMainActivity().onFragmentInteraction(OPEN_CHANNEL_FRAGMENT, null);
            assertTrue(getMainActivity().getCurrentFragment() instanceof ChannelFragment);

            getMainActivity().onFragmentInteraction(OPEN_EVENT_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof EventFragment);

            //getMainActivity().onFragmentInteraction(OPEN_EVENT_DETAIL_FRAGMENT, Utility.defaultEvent());
            //assertTrue(getMainActivity().getCurrentFragment() instanceof EventDetailFragment);

            getMainActivity().onFragmentInteraction(OPEN_ASSOCIATION_FRAGMENT, user);
            assertTrue(getMainActivity().getCurrentFragment() instanceof AssociationFragment);

            getMainActivity().onFragmentInteraction(OPEN_ASSOCIATION_DETAIL_FRAGMENT, Utility.defaultAssociation());
            assertTrue(getMainActivity().getCurrentFragment() instanceof AssociationDetailFragment);
        });
    }
}
