package be.kdg.twitterandroid;

import android.app.Application;

import be.kdg.twitterandroid.services.TweetService;

/**
 * Created by Maarten on 23/01/2016.
 */
public class TwitterAndroidApplication extends Application {
    private TweetService tweetService;

    public TweetService getTweetService() {
        return tweetService;
    }

    public void setTweetService(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
