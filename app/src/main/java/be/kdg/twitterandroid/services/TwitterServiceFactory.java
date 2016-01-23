package be.kdg.twitterandroid.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import be.kdg.twitterandroid.config.Constants;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import retrofit.RestAdapter;

/**
 * Created by Maarten on 14/01/2016.
 */
public class TwitterServiceFactory {
    private static TweetService tweetService;
    private static UserService userService;
    private static RestAdapter restAdapter;

    private TwitterServiceFactory(){}

    public static void setOAuthTokens(String token, String tokenSecret){
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                Constants.API_KEY,
                Constants.API_SECRET
        );
        consumer.setTokenWithSecret(token, tokenSecret);

        restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_ENDPOINT)
                .setClient(new SignedOkClient(consumer))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();
    }

    public static void setOAuthTokensFromSharedPreferences(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setOAuthTokens(prefs.getString("token", null), prefs.getString("tokenSecret", null));
    }

    public static synchronized TweetService getTweetService() {
        if(tweetService == null) tweetService = restAdapter.create(TweetService.class);
        return tweetService;
    }

    public static synchronized UserService getUserService() {
        if(userService == null) userService = restAdapter.create(UserService.class);
        return userService;
    }
}
