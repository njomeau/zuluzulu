package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;


// TODO: Add admin access, ending date, linked-chat id, linked-association id, position
public class Event implements Serializable {
    public final static List<String> FIELDS = Arrays.asList("id", "name", "short_desc", "long_desc", "start_date", "end_date", "likes");

    private int id;
    private String name;
    private String shortDesc;
    private String longDesc;

    private int likes;
    private String organizer;
    private String place;

    private String bannerUri;
    private String iconUri;

    private String url_place_and_room;
    private String website;
    private String contact;
    private String category;
    private String speaker;

    private EventDate date;

    private int channel_id;
    private int assos_id;

    private Event(int id, String name, String shortDesc, String longDesc, EventDate date,
                 int likes, String organizer, String place, String bannerUri, String iconUri,
                 String url_place_and_room, String website, String contact, String category, String speaker) {
        this.id = id;
        setName(name);
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        setDate(date);
        this.likes = likes;
        this.organizer = organizer;
        this.place = place;
        this.bannerUri = bannerUri;
        this.iconUri = iconUri;
        setWebsite(website);
        setUrlPlaceAndRoom(url_place_and_room);
        setContact(contact);
        setCategory(category);
        setSpeaker(speaker);
    }

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

        setUrlPlaceAndRoom(data.getString("url_place_and_room"));
        setWebsite(data.getString("website"));
        setContact(data.getString("contact"));
        setCategory(data.getString("category"));
        setSpeaker(data.getString("speaker"));

        String icon_str = data.getString("icon_uri");
        Uri uri = icon_str == null ? Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) : Uri.parse(icon_str);
        iconUri = uri == null ? null : uri.toString();

        // Init the Banner URI
        String banner_str = data.getString("banner_uri");
        uri = banner_str == null ? Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) : Uri.parse(banner_str);
        bannerUri = uri == null ? null : uri.toString();

        setDate(new EventDate(data.getDate("start_date"), data.getDate("end_date")));

        likes = data.getInteger("likes");

        channel_id = data.get("channel_id") == null ? 0 : data.getInteger("channel_id");

        assos_id = data.get("assos_id") == null ? 0 : data.getInteger("assos_id");
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

    public void setName(String name) {
        assert(name != null);
        this.name = name.trim().replaceAll("\"", "");
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public String getBannerUri() {
        return bannerUri;
    }

    public String getIconUri() {
        return iconUri;
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getPlace() {
        return place;
    }

    public Integer getLikes() {
        return likes;
    }

    public void increaseLikes() {
        likes += 1;
    }

    public void decreaseLikes() {
        likes -= 1;
    }

    public int getChannelId() {
        return channel_id;
    }

    public int getAssosId() {
        return assos_id;
    }

    public String getUrlPlaceAndRoom() {
        return url_place_and_room;
    }

    public void setUrlPlaceAndRoom(String url_place_and_room) {
        assert(url_place_and_room != null);

        this.url_place_and_room = url_place_and_room;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        assert(website != null);

        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        assert(contact != null);

        this.contact = contact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        assert(category != null);

        this.category = category;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        assert(speaker != null);

        this.speaker = speaker;
    }


    private void setDate(EventDate date) {
        assert(date != null);
        this.date = date;
    }

    public Date getStartDate() {
        return date.getStartDate();
    }

    public Date getEndDate() {
        return date.getEndDate();
    }

    public String getStartDateString() {
        return date.getStartDateString();
    }

    public String getEndDateString() {
        return date.getEndDateString();
    }

    public static final class EventBuilder{
        private int id;
        private String name;
        private String shortDesc;
        private String longDesc;
        private int likes;
        private String organizer;
        private String place;
        private String bannerUri;
        private String iconUri;
        private String url_place_and_room;
        private String website;
        private String contact;
        private String category;
        private String speaker;

        private EventDate date;

        private int channel_id;
        private int assos_id;

        public EventBuilder(){
            // set Default values
            this.likes = 0;
        }

        public Event build(){
            return new Event(id, name, shortDesc, longDesc, date, likes, organizer, place, bannerUri, iconUri, url_place_and_room, website, contact, category, speaker);
        }

        public EventBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public EventBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public EventBuilder setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
            return this;
        }

        public EventBuilder setLongDesc(String longDesc) {
            this.longDesc = longDesc;
            return this;
        }


        public EventBuilder setLikes(int likes) {
            this.likes = likes;
            return this;
        }

        public EventBuilder setOrganizer(String organizer) {
            this.organizer = organizer;
            return this;
        }

        public EventBuilder setPlace(String place) {
            this.place = place;
            return this;
        }

        public EventBuilder setBannerUri(String bannerUri) {
            this.bannerUri = bannerUri;
            return this;
        }

        public EventBuilder setIconUri(String iconUri) {
            this.iconUri = iconUri;
            return this;
        }

        public EventBuilder setUrlPlaceAndRoom(String url_place_and_room) {
            this.url_place_and_room = url_place_and_room;
            return this;
        }

        public EventBuilder setWebsite(String website) {
            this.website = website;
            return this;
        }

        public EventBuilder setContact(String contact) {
            this.contact = contact;
            return this;
        }

        public EventBuilder setCategory(String category) {
            this.category = category;
            return this;
        }

        public EventBuilder setSpeaker(String speaker) {
            this.speaker = speaker;
            return this;
        }

        public EventBuilder setDate(EventDate date) {
            this.date = date;
            return this;
        }

        public EventBuilder setChannelId(int channel_id) {
            this.channel_id = channel_id;
            return this;
        }

        public EventBuilder setAssosId(int assos_id) {
            this.assos_id = assos_id;
            return this;
        }
    }
}
