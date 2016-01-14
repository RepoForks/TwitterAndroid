package be.kdg.twitterandroid.activities;

import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;

/**
 * Created by Maarten on 14/01/2016.
 */
public interface TweetInteractionListener {
    void onTweetReplyClick(Tweet tweet);
    void onTweetRetweetClick(Tweet tweet);
    void onTweetHeartClick(Tweet tweet);
    void onTweetMenuClick(Tweet tweet);
    void onUserClick(User user);
}
