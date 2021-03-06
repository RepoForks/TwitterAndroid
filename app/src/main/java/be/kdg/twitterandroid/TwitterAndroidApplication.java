package be.kdg.twitterandroid;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TwitterServiceFactory;

/**
 * Created by Maarten on 23/01/2016.
 */
public class TwitterAndroidApplication extends Application {
    private User currentUser;
    private List<Tweet> tweets;
    private List<Tweet> profileTweets;
    private List<Tweet> hashtagTweets;

    public List<Tweet> getProfileTweets() {
        return profileTweets;
    }

    public List<Tweet> getHashtagTweets() {
        return hashtagTweets;
    }

    public void setHashtagTweets(List<Tweet> hashtagTweets) {
        this.hashtagTweets = hashtagTweets;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public boolean userHasAuthTokens(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPrefs.contains("token") && sharedPrefs.contains("tokenSecret");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.tweets = new ArrayList<>();
        this.profileTweets = new ArrayList<>();
        this.hashtagTweets = new ArrayList<>();

        if(userHasAuthTokens()){
            TwitterServiceFactory.setOAuthTokensFromSharedPreferences(this);
        }
    }
}
