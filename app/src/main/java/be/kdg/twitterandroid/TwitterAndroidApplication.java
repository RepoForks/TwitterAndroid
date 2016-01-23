package be.kdg.twitterandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TweetService;
import be.kdg.twitterandroid.services.TwitterServiceFactory;

/**
 * Created by Maarten on 23/01/2016.
 */
public class TwitterAndroidApplication extends Application {
    private TweetService tweetService;
    private User currentUser;
    private List<Tweet> tweets;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public TweetService getTweetService() {
        return tweetService;
    }

    public void setTweetService(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    private boolean userHasAuthTokens(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.contains("token") && sharedPrefs.contains("tokenSecret");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.tweets = new ArrayList<>();

        if(userHasAuthTokens()){
            TwitterServiceFactory twitterServiceFactory = new TwitterServiceFactory();
            twitterServiceFactory.setOAuthTokensFromSharedPreferences(this);
            this.tweetService = twitterServiceFactory.getTweetService();
        }
    }
}
