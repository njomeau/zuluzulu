package ch.epfl.sweng.zuluzulu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Adapters.PostArrayAdapter;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.User;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_CHAT_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_WRITE_POST_FRAGMENT;
import static ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment.PREF_KEY_ANONYM;

/**
 * A {@link SuperChatPostsFragment} subclass.
 * This fragment is used to display the posts
 */
public class PostFragment extends SuperChatPostsFragment {
    private List<Post> posts = new ArrayList<>();
    private PostArrayAdapter adapter;

    private Button writePostButton;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView filterTimeButton;
    private ImageView filterRepliesButton;
    private ImageView filterUpsButton;

    private String currentFilter;

    public PostFragment() {
        // Required empty public constructor
    }

    public static PostFragment newInstance(User user, Channel channel) {
        return (PostFragment) newInstanceOf("post", user, channel);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        listView = view.findViewById(R.id.posts_list_view);
        chatButton = view.findViewById(R.id.chat_button);
        postsButton = view.findViewById(R.id.posts_button);
        writePostButton = view.findViewById(R.id.posts_new_post_button);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_post);
        filterTimeButton = view.findViewById(R.id.post_filter_time);
        filterRepliesButton = view.findViewById(R.id.post_filter_nbReplies);
        filterUpsButton = view.findViewById(R.id.post_filter_nbUps);

        chatButton.setEnabled(true);
        postsButton.setEnabled(false);

        adapter = new PostArrayAdapter(view.getContext(), posts, user);
        listView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this::refresh);


        anonymous = getActivity().getPreferences(Context.MODE_PRIVATE).getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);
        currentFilter = "time";

        loadAllPosts();
        setUpChatButton();
        setUpNewPostButton();
        setUpFilterButtons();
        setUpReplyListener();

        return view;
    }

    /**
     * Add an onClick listener on the button to switch to the chat fragment
     */
    private void setUpChatButton() {
        chatButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_CHAT_FRAGMENT, channel));
    }

    private void loadAllPosts() {
        DatabaseFactory.getDependency().getPostsFromChannel(channel.getId(), result -> {
            posts.clear();
            posts.addAll(result);
            adapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    /**
     * Set up an onClick listener on the button to write a new post
     */
    private void setUpNewPostButton() {
        writePostButton.setOnClickListener(v -> mListener.onFragmentInteraction(OPEN_WRITE_POST_FRAGMENT, channel));
    }

    /**
     * This function is called when the user swipes down to refresh the list of posts
     */
    private void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        loadAllPosts();
    }

    /**
     * Set up the listener on a post to go to the reply fragment when we click on it
     */
    private void setUpReplyListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Post post = posts.get(position);
                mListener.onFragmentInteraction(CommunicationTag.OPEN_REPLY_FRAGMENT, post);
            }
        });
    }

    /**
     * Set up the onClick listeners on the filter buttons
     */
    private void setUpFilterButtons() {
        filterTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilter("time");
            }
        });

        filterRepliesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilter("nbResponses");
            }
        });

        filterUpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateFilter("nbUps");
            }
        });
    }

    /**
     * Update the current filter by the new one and update the list of the posts
     *
     * @param newFilter The new filter to apply
     */
    private void updateFilter(String newFilter) {
        if (!newFilter.equals(currentFilter)) {
            filterTimeButton.setImageResource(newFilter.equals("time") ? R.drawable.time_selected : R.drawable.time_notselected);
            filterRepliesButton.setImageResource(newFilter.equals("nbResponses") ? R.drawable.replies_selected : R.drawable.replies_notselected);
            filterUpsButton.setImageResource(newFilter.equals("nbUps") ? R.drawable.up_selected : R.drawable.up_notselected);

            currentFilter = newFilter;
            loadAllPosts();
        }
    }
}
