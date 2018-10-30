package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.Fragments.WebViewFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.tequila.AuthClient;
import ch.epfl.sweng.zuluzulu.tequila.AuthServer;
import ch.epfl.sweng.zuluzulu.tequila.OAuth2Config;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;


//import ch.epfl.sweng.zuluzulu.Fragments.EventDetailFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    // Const used to send a Increment or Decrement message
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SuperFragment current_fragment;
    private User user;

    // This resource is used for tests
    // That's the recommended way to implement it
    // @see https://developer.android.com/training/testing/espresso/idling-resource#integrate-recommended-approach
    private CountingIdlingResource resource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the resource
        resource = new CountingIdlingResource("Main Activity");

        // Needed to use Firebase storage and Firestore
        FirebaseApp.initializeApp(getApplicationContext());

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initial to guestUser
        this.user = new User.UserBuilder().buildGuestUser();

        navigationView = initNavigationView();
        initDrawerContent();

        Intent i = getIntent();

        String redirectURIwithCode = i.getStringExtra("redirectUri");
        if (redirectURIwithCode != null) {
            openLogin(redirectURIwithCode);
        } else {
            // Look if there is a user object set
            User user = (User) i.getSerializableExtra("user");
            if (user != null) {
                this.user = user;
                updateMenuItems();
            }

            selectItem(navigationView.getMenu().findItem(R.id.nav_main));
        }
    }

    /**
     * Open the login fragment when requested by redirect uri
     * @param redirectURIwithCode redirect url
     */
    private void openLogin(String redirectURIwithCode) {
        Bundle toSend = new Bundle(1);
        toSend.putString("", redirectURIwithCode);
        LoginFragment fragment = LoginFragment.newInstance();
        fragment.setArguments(toSend);
        openFragment(fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * Create the navigation view and the toolbar
     *
     * @return The navigation view
     */
    private NavigationView initNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        return navigationView;
    }

    /**
     * Attach the drawer_view to the navigation view and set a listener on the menu
     */
    private void initDrawerContent() {
        updateMenuItems();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (!menuItem.isChecked()) {
                            selectItem(menuItem);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    /**
     * Attach the drawer_view to the navigation view
     */
    private void updateMenuItems() {
        navigationView.getMenu().clear();
        if (getUser().hasRole(UserRole.ADMIN)) {
            navigationView.inflateMenu(R.menu.drawer_view_admin);
        } else if (isAuthenticated()) {
            navigationView.inflateMenu(R.menu.drawer_view_user);
        } else {
            navigationView.inflateMenu(R.menu.drawer_view_guest);
        }
    }

    /**
     * Return true if the user is connected
     *
     * @return boolean
     */
    public boolean isAuthenticated() {
        return user.isConnected();
    }

    /**
     * Create a new fragment and replace it in the activity
     *
     * @param menuItem The item that corresponds to a fragment on the menu
     */

    private void selectItem(MenuItem menuItem) {

        SuperFragment fragment;

        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_login:
                //to set arguments for the login
                fragment = LoginFragment.newInstance();
                break;
            case R.id.nav_about:
                fragment = AboutZuluzuluFragment.newInstance();
                break;
            case R.id.nav_associations:
                fragment = AssociationFragment.newInstance(user);
                break;
            case R.id.nav_events:
                fragment = EventFragment.newInstance(user);
                break;
            case R.id.nav_settings:
                fragment = SettingsFragment.newInstance();
                break;
            case R.id.nav_profile:
                fragment = ProfileFragment.newInstance(user);
                break;
            case R.id.nav_logout:
                this.user = new User.UserBuilder().buildGuestUser();

                android.webkit.CookieManager.getInstance().removeAllCookie();

                updateMenuItems();
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_chat:
                fragment = ChannelFragment.newInstance(user);
                break;
            case R.id.nav_associations_generator:
                fragment = AssociationsGeneratorFragment.newInstance(user);
                break;
            default:
                fragment = MainFragment.newInstance(user);
        }

        if (openFragment(fragment)) {
            // Opening the fragment worked
            menuItem.setChecked(true);
        }
    }


    public boolean openFragment(SuperFragment fragment) {

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                current_fragment = fragment;
                return true;
            }
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(CommunicationTag tag, Object data) {
        switch (tag) {

            case SET_USER:
                Map<Integer, Object> received = (HashMap<Integer,Object>) data;
                this.user = (User) received.get(0);
                updateMenuItems();
                break;
            case OPENING_WEBVIEW:
                openWebViewFragment((String) data);
                break;
            case INCREMENT_IDLING_RESOURCE:
                incrementCountingIdlingResource();
                break;
            case DECREMENT_IDLING_RESOURCE:
                decrementCountingIdlingResource();
                break;
            case SET_TITLE:
                setTitle((String)data);
                break;
            case OPEN_CHAT_FRAGMENT:
                int channelID = (Integer) data;
                openFragment(ChatFragment.newInstance(user, channelID));
                break;
            case OPEN_ASSOCIATION_FRAGMENT:
                openFragment(AssociationFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_associations));
                break;
            case OPEN_ASSOCIATION_DETAIL_FRAGMENT:
                Association association = (Association) data;
                openFragment(AssociationDetailFragment.newInstance(user, association));
                break;
            case OPEN_ABOUT_US_FRAGMENT:
                openFragment(AboutZuluzuluFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_about));
                break;
            case OPEN_MAIN_FRAGMENT:
                openFragment(MainFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_main));
                break;
            case OPEN_EVENT_FRAGMENT:
                openFragment(EventFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_events));
                break;
            case OPEN_EVENT_DETAIL_FRAGMENT:
                Event event = (Event) data;
                // openFragment(EventDetailFragment.newInstance(user, event));
                break;
            case OPEN_CHANNEL_FRAGMENT:
                openFragment(ChannelFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_chat));
                break;
            case OPEN_LOGIN_FRAGMENT:
                openFragment(LoginFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_login));
                break;
            case OPEN_PROFILE_FRAGMENT:
                openFragment(ProfileFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_profile));
                break;
            case OPEN_SETTINGS_FRAGMENT:
                openFragment(SettingsFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_settings));
                break;
            default:
                // Should never happen
                throw new AssertionError(tag);
        }
    }

    /**
     * Gaultier code to open a webview fragment
     * Refactored in a new function
     * @param url web view url
     */
    private void openWebViewFragment(String url) {
        Bundle toSend = new Bundle(1);
        toSend.putString("",url);
        WebViewFragment fragment = WebViewFragment.newInstance();
        fragment.setArguments(toSend);
        openFragment(fragment);
    }

    /**
     * Return the current fragment
     *
     * @return current fragment
     */
    public SuperFragment getCurrentFragment() {
        return current_fragment;
    }

    /**
     * Return the current user
     *
     * @return current user
     */
    public User getUser() {
        return user;
    }

    /**
     * Increment the countingIdlingResource
     * Do this before a async task
     */
    public void incrementCountingIdlingResource() {
        resource.increment();
    }

    /**
     * Decrement the countingIdlingResource
     * Do this after a async task
     */
    public void decrementCountingIdlingResource() {
        resource.decrement();
    }

    /**
     * Return the resource for the tests
     *
     * @return resource
     */
    public CountingIdlingResource getCountingIdlingResource() {
        return resource;
    }
}
