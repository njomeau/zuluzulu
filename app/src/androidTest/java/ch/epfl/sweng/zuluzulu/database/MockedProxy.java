package ch.epfl.sweng.zuluzulu.database;

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.firebase.OnResult;
import ch.epfl.sweng.zuluzulu.firebase.Proxy;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.Utility;

import static ch.epfl.sweng.zuluzulu.Utility.createFilledUserBuilder;
import static ch.epfl.sweng.zuluzulu.Utility.createTestAuthenticated;
import static ch.epfl.sweng.zuluzulu.Utility.defaultPost0;
import static ch.epfl.sweng.zuluzulu.Utility.defaultPost1;

public class MockedProxy implements Proxy {

    private final Map<String, Association> associationMap = new HashMap<String, Association>() {{
        put("0", Utility.defaultAssociation());
    }};

    private final Map<String, Event> eventMap = new HashMap<String, Event>() {{
        put("0", Utility.defaultEvent().build());
        put("1", Utility.currentTimeEvent().build());
    }};


    private final Map<String, ChannelRepresentation> channelMap = new HashMap<String, ChannelRepresentation>() {{
        ChannelRepresentation rep = new ChannelRepresentation(Utility.defaultChannel());
        rep.messageMap.put("0", Utility.defaultMessage0());
        rep.messageMap.put("1", Utility.defaultMessage1());
        rep.postMap.put("0", new Pair<>(defaultPost0(), new HashMap<>()));
        rep.postMap.put("1", new Pair<>(defaultPost1(), new HashMap<>()));
        put("0", rep);
    }};

    private final Map<String, AuthenticatedUser> userMap = new HashMap<String, AuthenticatedUser>() {{
        put("1", createFilledUserBuilder().setSciper("1").buildAdmin());
        put("0", createTestAuthenticated());
    }};

    @Override
    public String getNewChannelId() {
        return String.valueOf(channelMap.size());
    }

    @Override
    public String getNewEventId() {
        return String.valueOf(eventMap.size());
    }

    @Override
    public String getNewAssociationId() {
        return String.valueOf(associationMap.size());
    }

    @Override
    public String getNewPostId(String channelId) {
        if (!channelMap.containsKey(channelId))
            return "0";
        return String.valueOf(Objects.requireNonNull(channelMap.get(channelId)).postMap.size());
    }

    @Override
    public String getNewMessageId(String channelId) {
        if (!channelMap.containsKey(channelId))
            return "0";
        return String.valueOf(Objects.requireNonNull(channelMap.get(channelId)).messageMap.size());
    }

    @Override
    public String getNewReplyId(String channelId, String originalPostId) {
        if (!channelMap.containsKey(channelId) || !Objects.requireNonNull(channelMap.get(channelId)).postMap.containsKey(originalPostId))
            return "0";
        return String.valueOf(Objects.requireNonNull(channelMap.get(channelId)).postMap.get(originalPostId).second.size());
    }

    @Override
    public void addAssociation(Association association) {
        associationMap.put(association.getId(), association);
    }

    @Override
    public void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    @Override
    public void addMessage(ChatMessage message) {
        if (!channelMap.containsKey(message.getChannelId()))
            return;
        Objects.requireNonNull(channelMap.get(message.getChannelId())).messageMap.put(message.getId(), message);
    }

    @Override
    public void addPost(Post post) {
        if (!channelMap.containsKey(post.getChannelId()))
            return;
        Objects.requireNonNull(channelMap.get(post.getChannelId())).postMap.put(post.getId(), new Pair<>(post, Collections.EMPTY_MAP));
    }

    @Override
    public void addReply(Post post) {
        if (!channelMap.containsKey(post.getChannelId()) || !Objects.requireNonNull(channelMap.get(post.getChannelId())).postMap.containsKey(post.getId()))
            return;
        Objects.requireNonNull(channelMap.get(post.getChannelId())).postMap.get(post.getId()).second.put(post.getId(), post);
    }

    @Override
    public void updateUser(AuthenticatedUser user) {
        userMap.put(user.getSciper(), user);
    }

    @Override
    public void getAllChannels(OnResult<List<Channel>> onResult) {
        ArrayList<Channel> result = new ArrayList<>();
        for (ChannelRepresentation channel : channelMap.values())
            result.add(channel.channel);

        onResult.apply(result);
    }

    @Override
    public void getAllEvents(OnResult<List<Event>> onResult) {
        ArrayList<Event> result = new ArrayList<>(eventMap.values());

        onResult.apply(result);
    }

    @Override
    public void getEventsFromToday(OnResult<List<Event>> onResult, int limit) {
        ArrayList<Event> result = new ArrayList<>(eventMap.values());

        onResult.apply(result);
    }

    @Override
    public void getAllAssociations(OnResult<List<Association>> onResult) {
        ArrayList<Association> result = new ArrayList<>(associationMap.values());

        onResult.apply(result);
    }

    @Override
    public void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult) {
        if (ids == null)
            return;

        Log.d("GET_CHAN", "");
        ArrayList<Channel> result = new ArrayList<>();
        for (String id : ids)
            if (channelMap.containsKey(id))
                result.add(Objects.requireNonNull(channelMap.get(id)).channel);
        onResult.apply(result);
    }

    @Override
    public void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult) {
        if (ids == null)
            return;

        ArrayList<Event> result = new ArrayList<>();
        for (Event event : eventMap.values())
            if (ids.contains(event.getId()))
                result.add(event);
        onResult.apply(result);
    }

    @Override
    public void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult) {
        if (ids == null)
            return;

        ArrayList<Association> result = new ArrayList<>();
        for (Association association : associationMap.values())
            if (ids.contains(association.getId()))
                result.add(association);
        onResult.apply(result);
    }

    @Override
    public void getChannelFromId(String id, OnResult<Channel> onResult) {
        if (id != null && channelMap.containsKey(id))
            onResult.apply(Objects.requireNonNull(channelMap.get(id)).channel);
    }

    @Override
    public void getEventFromId(String id, OnResult<Event> onResult) {
        if (id != null && eventMap.containsKey(id))
            onResult.apply(eventMap.get(id));
    }

    @Override
    public void getAssociationFromId(String id, OnResult<Association> onResult) {
        if (id != null && associationMap.containsKey(id))
            onResult.apply(associationMap.get(id));
    }

    @Override
    public void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {
        // too hard to mock sorry :(
    }

    @Override
    public void getMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {
        if (channelId != null && channelMap.containsKey(channelId)) {
            ArrayList<ChatMessage> result = new ArrayList<>(Objects.requireNonNull(channelMap.get(channelId)).messageMap.values());
            onResult.apply(result);
        }
    }

    @Override
    public void getPostsFromChannel(String channelId, OnResult<List<Post>> onResult) {
        //TODO nico il faut ajouter le post dans la liste du channel... mais comment ? ici c'est pas idéal
        if (channelId != null && channelMap.containsKey(channelId)) {
            ArrayList<Post> result = new ArrayList<>();
            for (Pair<Post, Map<String, Post>> pair : Objects.requireNonNull(channelMap.get(channelId)).postMap.values()) {
                result.add(pair.first);
            }
            onResult.apply(result);
        }
    }

    @Override
    public void addChannelToUserFollowedChannels(Channel channel, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void addEventToUserFollowedEvents(Event event, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void addAssociationToUserFollowedAssociations(Association association, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void removeChannelFromUserFollowedChannels(Channel channel, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void removeEventFromUserFollowedEvents(Event event, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void removeAssociationFromUserFollowedAssociations(Association association, AuthenticatedUser user) {
        // TODO: Change if the implementation is changed.
    }

    @Override
    public void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult) {
        if (channelId != null && channelMap.containsKey(channelId) && postId != null && Objects.requireNonNull(channelMap.get(channelId)).postMap.containsKey(postId)) {
            ArrayList<Post> result = new ArrayList<>(Objects.requireNonNull(channelMap.get(channelId)).postMap.get(postId).second.values());
            onResult.apply(result);
        }
    }

    @Override
    public void getUserWithIdOrCreateIt(String sciper, OnResult<AuthenticatedUser> onResult) {
        if (sciper != null && userMap.containsKey(sciper)) {
            onResult.apply(userMap.get(sciper));
        } else if (sciper != null && !userMap.containsKey(sciper)) {
            User.UserBuilder b = new User.UserBuilder();
            b.setEmail(sciper + "@epfl.ch");
            b.setFirst_names("Je suis un test");
            b.setLast_names("dotCom");
            b.setFollowedAssociations(new ArrayList<>());
            b.setFollowedChannels(new ArrayList<>());
            b.setFollowedEvents(new ArrayList<>());
            b.setGaspar(sciper);
            b.setSciper(sciper);
            b.setSection("IN"); // TOUS EN IN!!!!
            b.setSemester("BA5");
            AuthenticatedUser user = b.buildAuthenticatedUser();
            if (user != null) {
                userMap.put(sciper, user);
                onResult.apply(userMap.get(sciper));
            }
        }
    }

    @Override
    public void getAllUsers(OnResult<List<Map<String, Object>>> onResult) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (AuthenticatedUser user : userMap.values()) {
            Map<String, Object> map = new HashMap<>();
            map.put("sciper", user.getSciper());
            map.put("roles", user.getRoles());
            map.put("followed_events", user.getFollowedEvents());
            map.put("followed_associations", user.getFollowedAssociations());
            map.put("followed_channels", user.getFollowedChannels());
            result.add(map);
        }
        onResult.apply(result);
    }

    @Override
    public void updateUserRole(String sciper, List<String> roles) {
        if (userMap.containsKey(sciper))
            Objects.requireNonNull(userMap.get(sciper)).setRoles(roles);
    }

    @Override
    public void updatePost(Post post) {
        // too hard to mock sorry :(
    }

    private final class ChannelRepresentation {
        private final Channel channel;
        private final Map<String, ChatMessage> messageMap;
        private final Map<String, Pair<Post, Map<String, Post>>> postMap;

        private ChannelRepresentation(Channel channel) {
            this.channel = channel;
            messageMap = new HashMap<>();
            postMap = new HashMap<>();
        }
    }
}
