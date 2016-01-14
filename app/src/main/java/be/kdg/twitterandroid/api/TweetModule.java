package be.kdg.twitterandroid.api;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.basic.DefaultOAuthConsumer;
import retrofit.RestAdapter;

/**
 * Created by Maarten on 14/01/2016.
 */
public class TweetModule {
    private TweetService tweetService;

    public void init(String token, String tokenSecret){
        OAuthConsumer consumer = new DefaultOAuthConsumer(
                "OtL5mNMElZBZFzcMjOQCULI1P",                          // API key
                "Bw23dovGvdHSoij3eyjwSh5elXs4X17f8my1eN04rcbOT3Fhzw"  // API secret
        );
        consumer.setTokenWithSecret(token, tokenSecret);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.twitter.com/1.1/")
                .setClient(new SignedOkClient(consumer))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        tweetService = restAdapter.create(TweetService.class);
    }

    public TweetService getTweetService() {
        return tweetService;
    }
}
