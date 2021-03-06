package ch.epfl.sweng.zuluzulu.structure;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.firebase.FirebaseMapDecorator;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PostTest {

    private static final String currentUserId = "000";
    private static final String channelId = "001";

    private static final String id1 = "1";
    private static final String originalPostId1 = "002";
    private static final String senderName1 = "James";
    private static final String sciper1 = "111111";
    private static final String message1 = "James message";
    private static final String color1 = "#e8b30f";
    private static final Date time = new Date();
    private final List<String> upScipers1 = singletonList(currentUserId);
    private final List<String> downScipers1 = new ArrayList<>();

    private static final String id2 = "2";
    private static final String originalPostId2 = null;
    private static final String senderName2 = "";
    private static final String sciper2 = "222222";
    private static final String message2 = "Bond's message";
    private static final String color2 = "#f8b30f";
    private final List<String> upScipers2 = new ArrayList<>();
    private final List<String> downScipers2 = singletonList(currentUserId);

    private Post post1;
    private Post post2;

    @Before
    public void init() {
        post1 = new Post(id1,
                channelId,
                originalPostId1,
                message1,
                senderName1,
                sciper1,
                time,
                color1,
                new ArrayList<>(),
                new ArrayList<>(upScipers1),
                new ArrayList<>(downScipers1)
        );

        post2 = new Post(id2,
                channelId,
                originalPostId2,
                message2,
                senderName2,
                sciper2,
                time,
                color2,
                new ArrayList<>(singletonList(id2)),
                new ArrayList<>(upScipers2),
                new ArrayList<>(downScipers2)
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIncorrectDataConstructor() {
        new Post(new FirebaseMapDecorator(Collections.singletonMap("id", "lol")));
    }

    @Test
    public void fmapConstructorTest() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", id1);
        map.put("channel_id", channelId);
        map.put("original_post_id", originalPostId1);
        map.put("sender_name", senderName1);
        map.put("message", message1);
        map.put("time", time);
        map.put("sender_sciper", sciper1);
        map.put("color", color1);
        map.put("replies", new ArrayList<>());
        map.put("up_scipers", upScipers1);
        map.put("down_scipers", downScipers1);
        assertEquals(map, new Post(new FirebaseMapDecorator(map)).getData());
    }

    @Test
    public void upvoteDownvote() {
        assertTrue(post1.isUpByUser(currentUserId));
        assertFalse(post1.upvoteWithUser(currentUserId));
        assertFalse(post1.downvoteWithUser(currentUserId));

        assertFalse(post1.isUpByUser("3"));
        assertTrue(post1.upvoteWithUser("3"));
        assertFalse(post2.isDownByUser("3"));
        assertTrue(post2.downvoteWithUser("3"));

        assertTrue(post2.isDownByUser(currentUserId));
        assertFalse(post2.upvoteWithUser(currentUserId));
        assertFalse(post2.downvoteWithUser(currentUserId));
    }

    @Test
    public void testGetters() {
        assertEquals(senderName1, post1.getSenderName());
        assertEquals(sciper1, post1.getSenderSciper());
        assertEquals(message1, post1.getMessage());
        assertEquals(time, post1.getTime());
        assertEquals(color1, post1.getColor());
        assertEquals(1, post1.getNbUps());
        assertEquals(1, post2.getNbReplies());
        assertEquals(singletonList(id2), post2.getReplies());
        assertEquals(channelId, post1.getChannelId());
        assertEquals(id1, post1.getId());

        assertTrue(post1.isReply());
        assertFalse(post2.isReply());
        assertEquals(originalPostId1, post1.getOriginalPostId());
        assertEquals(originalPostId2, post2.getOriginalPostId());
    }

    @Test
    public void testIsAnonymous() {
        assertFalse(post1.isAnonymous());
        assertTrue(post2.isAnonymous());
    }
}
