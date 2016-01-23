package be.kdg.twitterandroid.api;

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
    private TweetService tweetService;

    public void setOAuthTokens(String token, String tokenSecret){
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                Constants.API_KEY,
                Constants.API_SECRET
        );
        consumer.setTokenWithSecret(token, tokenSecret);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_ENDPOINT)
                .setClient(new SignedOkClient(consumer))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .build();

        tweetService = restAdapter.create(TweetService.class);
    }

    public void setOAuthTokensFromSharedPreferences(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setOAuthTokens(prefs.getString("token", null), prefs.getString("tokenSecret", null));
    }

    public TweetService getTweetService() {
        return tweetService;
    }
}
