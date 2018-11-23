package ch.epfl.sweng.zuluzulu.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.Utility.PostColor;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.OPEN_POST_FRAGMENT;

/**
 * A {@link SuperFragment} subclass.
 * This fragment is used to write new posts
 */
public class WritePostFragment extends SuperFragment {
    private static final String ARG_USER = "ARG_USER";
    private static final String ARG_CHANNEL = "ARG_CHANNEL";
    private static final int POST_MAX_LENGTH = 200;

    private ConstraintLayout layout;
    private EditText editText;
    private Button sendButton;

    private AuthenticatedUser user;
    private Channel channel;
    private PostColor color;
    private boolean anonymous;

    public WritePostFragment() {
        // Required empty public constructor
    }

    public static WritePostFragment newInstance(User user, Channel channel) {
        WritePostFragment fragment = new WritePostFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        args.putSerializable(ARG_CHANNEL, channel);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (AuthenticatedUser) getArguments().getSerializable(ARG_USER);
            channel = (Channel) getArguments().getSerializable(ARG_CHANNEL);
            mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, channel.getName());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_post, container, false);

        color = PostColor.getRandomColor();
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        anonymous = preferences.getBoolean(SettingsFragment.PREF_KEY_ANONYM, false);

        layout = view.findViewById(R.id.write_post_layout);
        editText = view.findViewById(R.id.write_post_textEdit);
        sendButton = view.findViewById(R.id.write_post_send_button);

        layout.setBackgroundColor(Color.parseColor(color.getValue()));
        sendButton.setEnabled(false);

        setUpSendButton();
        setUpColorListener();
        setUpTextEditListener();

        return view;
    }

    /**
     * Set up an onClick listener on the send button
     */
    private void setUpSendButton() {
        sendButton.setOnClickListener(v -> {
            String senderName = anonymous ? "" : user.getFirstNames();
            String message = editText.getText().toString();
            Timestamp time = Timestamp.now();
            String sciper = user.getSciper();

            Map<String, Object> data = new HashMap();
            data.put("id", FirebaseProxy.getInstance().getNewPostId(channel.getId()));
            data.put("channel_id", channel.getId());
            data.put("sender_name", senderName);
            data.put("message", message);
            data.put("time", time.toDate());
            data.put("sciper", sciper);
            data.put("color", color.getValue());
            data.put("nb_ups", 0L);
            data.put("nb_responses", 0L);
            data.put("up_scipers", new ArrayList<>());
            data.put("down_scipers", new ArrayList<>());
            Post post = new Post(new FirebaseMapDecorator(data));
            FirebaseProxy.getInstance().addPost(post);
            mListener.onFragmentInteraction(OPEN_POST_FRAGMENT, channel);
        });
    }

    /**
     * Set up an onClick listener on the layout to be able to change the color of the post
     */
    private void setUpColorListener() {
        layout.setOnClickListener(v -> {
            color = PostColor.getRandomColorButNot(color);
            layout.setBackgroundColor(Color.parseColor(color.getValue()));
        });
    }

    /**
     * Set up an onTextChanged listener on the message field to know when the post is ready to be sent
     */
    private void setUpTextEditListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int messageLength = s.toString().length();
                boolean correctFormat = 0 < messageLength && messageLength < POST_MAX_LENGTH;;
                sendButton.setEnabled(correctFormat);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
}
