package be.kdg.twitterandroid.ui.activities.listeners;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import java.util.List;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.ui.activities.MainActivity;
import be.kdg.twitterandroid.ui.activities.ProfileActivity;
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
    private Activity activity;

    public DefaultTweetInteractionHandler(List<Tweet> tweets, View snackbarView, Activity activity) {
        this.tweets = tweets;
        this.snackbarView = snackbarView;
        this.activity = activity;
    }

    public void setTweetAdapter(TweetAdapter tweetAdapter) {
        this.tweetAdapter = tweetAdapter;
    }

    @Override
    public void onTweetReplyClick(Tweet tweet) {
        if(!(activity instanceof MainActivity)) return;
        ((MainActivity)activity).showCreateTweetPopupReply(tweet.getId(), tweet.getUser().getScreen_name());
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
    public void onTweetMenuClick(final Tweet tweet, View view) {
        PopupMenu menu = new PopupMenu(snackbarView.getContext(), view);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.item_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, tweet.getShareURL());
                        sendIntent.setType("text/plain");
                        activity.startActivity(sendIntent);
                        break;

                    case R.id.item_mute:
                        muteUser(tweet);
                        break;

                    case R.id.item_unmute:
                        unmuteUser(tweet);
                        break;

                    case R.id.item_block:
                        blockUser(tweet);
                        break;

                    case R.id.item_unblock:
                        unblockUser(tweet);
                        break;

                    case R.id.item_delete:
                        deleteTweet(tweet);
                        break;
                }

                return false;
            }
        });

        menu.inflate(R.menu.menu_tweet);
        if(tweet.getUser().isMuted()){
            menu.getMenu().findItem(R.id.item_mute).setVisible(false);
            menu.getMenu().findItem(R.id.item_unmute).setVisible(true);
        }
        if(tweet.getUser().isBlocked()){
            menu.getMenu().findItem(R.id.item_block).setVisible(false);
            menu.getMenu().findItem(R.id.item_unblock).setVisible(true);
        }
        if(activity instanceof MainActivity && ((TwitterAndroidApplication)activity.getApplication()).getCurrentUser().getId() == tweet.getUser().getId()){
            menu.getMenu().findItem(R.id.item_delete).setVisible(true);
        }
        menu.show();
    }

    private void muteUser(Tweet tweet){
        final int tweetIndex = tweets.indexOf(tweet);
        TwitterServiceFactory.getUserService().muteUser(tweet.getUser().getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (response.isSuccess()) {
                    tweets.get(tweetIndex).getUser().setMuted(true);
                    Snackbar.make(snackbarView, "User " + response.body().getScreen_name() + " muted", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Failed to mute user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void unmuteUser(Tweet tweet) {
        final int tweetIndex = tweets.indexOf(tweet);
        TwitterServiceFactory.getUserService().unmuteUser(tweet.getUser().getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (response.isSuccess()) {
                    tweets.get(tweetIndex).getUser().setMuted(false);
                    Snackbar.make(snackbarView, "User " + response.body().getScreen_name() + " unmuted", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Failed to unmute user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void blockUser(Tweet tweet) {
        final int tweetIndex = tweets.indexOf(tweet);
        TwitterServiceFactory.getUserService().blockUser(tweet.getUser().getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (response.isSuccess())
                    tweets.get(tweetIndex).getUser().setBlocked(true);
                    Snackbar.make(snackbarView, "User " + response.body().getScreen_name() + " blocked", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Failed to block user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void unblockUser(Tweet tweet) {
        final int tweetIndex = tweets.indexOf(tweet);
        TwitterServiceFactory.getUserService().unblockUser(tweet.getUser().getId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if (response.isSuccess())
                    tweets.get(tweetIndex).getUser().setBlocked(false);
                Snackbar.make(snackbarView, "User " + response.body().getScreen_name() + " unblocked", Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Failed to unblock user", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteTweet(Tweet tweet){
        final int tweetindex = tweets.indexOf(tweet);
        TwitterServiceFactory.getTweetService().deleteTweet(tweet.getId()).enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Response<Tweet> response) {
                if(response.isSuccess()){
                    tweets.remove(tweetindex);
                    tweetAdapter.notifyDataSetChanged();
                    Snackbar.make(snackbarView, "Tweet successfully deleted", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(snackbarView, "Failed to delete tweet", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(snackbarView, "Error: " + t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onUserClick(User user) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("userId", user.getId());
        activity.startActivity(intent);
    }

    @Override
    public void onHashtagClick(String hashtag) {
        Snackbar.make(snackbarView, "#" + hashtag + " clicked", Snackbar.LENGTH_SHORT).show();
    }
}
