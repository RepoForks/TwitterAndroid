package be.kdg.twitterandroid.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.util.List;

import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.ui.activities.listeners.TweetInteractionListener;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Maarten on 25/01/2016.
 */
public class DefaultTweetInteractionHandler implements TweetInteractionListener {
    private TweetAdapter tweetAdapter;
    private View snackbarView;
    private List<Tweet> tweets;

    public DefaultTweetInteractionHandler(List<Tweet> tweets, View snackbarView) {
        this.tweets = tweets;
        this.snackbarView = snackbarView;
    }

    public void setTweetAdapter(TweetAdapter tweetAdapter) {
        this.tweetAdapter = tweetAdapter;
    }

    @Override
    public void onTweetReplyClick(Tweet tweet) {
        Snackbar.make(snackbarView, tweet.getId() + " reply", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTweetRetweetClick(final Tweet tweet) {
        final int tweetindex = tweets.indexOf(tweet);

        Callback<Tweet> updateRetweetedStatus = new Callback<Tweet>() {
            @Override
            public void onResponse(Response<Tweet> response) {
                tweets.get(tweetindex).setRetweet_count(response.body().getRetweet_count());
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                tweets.get(tweetindex).setRetweeted(!tweet.isRetweeted()); // Roll back retweet status if retweeting failed
                tweetAdapter.notifyDataSetChanged();
            }
        };

        if(tweet.isRetweeted()){
            TwitterServiceFactory.getTweetService().unretweetTweet(tweet.getId()).enqueue(updateRetweetedStatus);
        } else {
            TwitterServiceFactory.getTweetService().retweetTweet(tweet.getId()).enqueue(updateRetweetedStatus);
        }

        tweets.get(tweetindex).setRetweeted(!tweet.isRetweeted()); // Already change the retweet status in the UI
        tweetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTweetHeartClick(final Tweet tweet) {
        final int tweetindex = tweets.indexOf(tweet);

        Callback<Tweet> updateFavoritedStatus = new Callback<Tweet>() {
            @Override
            public void onResponse(Response<Tweet> response) {
                tweets.get(tweetindex).setFavorite_count(response.body().getFavorite_count());
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                tweets.get(tweetindex).setFavorited(!tweet.isFavorited()); // Roll back favorite status if favoriting failed
                tweetAdapter.notifyDataSetChanged();
            }
        };

        if(tweet.isFavorited()){
            TwitterServiceFactory.getTweetService().unfavoriteTweet(tweet.getId()).enqueue(updateFavoritedStatus);
        } else {
            TwitterServiceFactory.getTweetService().favoriteTweet(tweet.getId()).enqueue(updateFavoritedStatus);
        }

        tweets.get(tweetindex).setFavorited(!tweet.isFavorited()); // Already change the favorite status in the UI
        tweetAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTweetMenuClick(Tweet tweet) {
        Snackbar.make(snackbarView, tweet.getId() + " menu", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClick(User user) {
        Snackbar.make(snackbarView, user.getName() + " clicked", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onHashtagClick(String hashtag) {
        Snackbar.make(snackbarView, "#" + hashtag + " clicked", Snackbar.LENGTH_SHORT).show();
    }
}
