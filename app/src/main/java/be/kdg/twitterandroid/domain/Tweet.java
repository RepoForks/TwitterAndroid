package be.kdg.twitterandroid.domain;

/**
 * Created by Maarten on 5/01/2016.
 */
public class Tweet {
    private long id,
            in_reply_to_status_id,
            in_reply_to_user_id,
            quoted_status_id;

    private boolean favorited,
            possibly_sensitive,
            retweeted,
            truncated,
            withheld_copyright;

    private String created_at,
            filter_level,
            id_str,
            in_reply_to_screen_name,
            in_reply_to_status_id_str,
            in_reply_to_user_id_str,
            lang,
            quoted_status_id_str,
            source,
            text,
            withheld_in_countries,
            withheld_scope;

    private int favorite_count,
            retweet_count;

    private Entities entities;

    private Place place;

    private Tweet quoted_status, retweeted_status;

    private User user;
}
