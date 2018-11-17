package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;


// TODO: Add admin access, ending date, linked-chat id, linked-association id, position
public class Event implements Serializable {
    public final static List<String> FIELDS = Arrays.asList("id", "name", "short_desc", "long_desc", "start_date", "likes");

    private int id;
    private String name;
    private String shortDesc;
    private String longDesc;

    private Date startDate;
    private String start_date_string;
    private int likes;
    private String organizer;
    private String place;

    private String bannerUri;
    private String iconUri;


    /**
     * Create an event using a FirebaseMap
     *
     * @param data the FirebaseMap
     * @throws IllegalArgumentException if the FirebaseMap isn't an Event's FirebaseMap
     */
    public Event(FirebaseMapDecorator data) {
        if (!data.hasFields(FIELDS))
            throw new IllegalArgumentException();

        id = data.getInteger("id");
        name = data.getString("name");
        shortDesc = data.getString("short_desc");
        longDesc = data.getString("long_desc");
        likes = data.getInteger("likes");
        organizer = data.getString("organizer");
        place = data.getString("place");

        shortDesc = data.getString("short_desc");
        longDesc = data.getString("long_desc");

        iconUri = data.getString("icon_uri");
        bannerUri = null; //data.getString("banner_uri");

        startDate = data.getDate("start_date");
        start_date_string = Utils.dateFormat.format(startDate);

        likes = data.getInteger("likes");
    }


    public static Comparator<Event> assoNameComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    public static Comparator<Event> dateComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        };
    }

    public static Comparator<Event> likeComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getLikes().compareTo(o1.getLikes());
            }
        };
    }

    // TODO: Add a method to add/remove one User to admins (instead of getting/setting the admins list)
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        return start_date_string;
    }

    public Uri getBannerUri() {
        return bannerUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(bannerUri);
    }

    public Uri getIconUri() {
        return iconUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(iconUri);
    }

    public String getOrganizer() { return organizer; }

    public String getPlace() { return place; }

    public Integer getLikes() { return likes; }

    public void increaseLikes() {
        likes += 1;
    }

    public void decreaseLikes() {
        likes -= 1;
    }

}
