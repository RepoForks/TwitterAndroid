package be.kdg.twitterandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.activities.TweetInteractionListener;
import be.kdg.twitterandroid.domain.Tweet;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Maarten on 14/01/2016.
 */
public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.TweetViewHolder> {
    private List<Tweet> tweets;
    private TweetInteractionListener interactionListener;
    private Context context;

    public TweetAdapter(List<Tweet> tweets, TweetInteractionListener interactionListener, Activity activity) {
        this.tweets = tweets;
        this.interactionListener = interactionListener;
        this.context = activity;
    }

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_tweet, parent, false);

        return new TweetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder tweetView, int position) {
        final Tweet tweet = tweets.get(position);

        if(tweet.getRetweeted_status() != null){
            tweetView.icRetweeted.setVisibility(View.VISIBLE);
            tweetView.tweetRetweetedName.setVisibility(View.VISIBLE);
            tweetView.tweetRetweetedName.setText(String.format("%s retweeted", tweet.getUser().getName()));
            tweetView.tweetName.setText(tweet.getRetweeted_status().getUser().getName());
            tweetView.tweetUsername.setText(String.format("@%s", tweet.getRetweeted_status().getUser().getScreen_name()));
            Picasso.with(context)
                    .load(tweet.getRetweeted_status().getUser().getProfile_image_url())
                    .into(tweetView.tweetProfilePic);
        } else {
            tweetView.icRetweeted.setVisibility(View.GONE);
            tweetView.tweetRetweetedName.setVisibility(View.GONE);
            tweetView.tweetName.setText(tweet.getUser().getName());
            tweetView.tweetUsername.setText(String.format("@%s", tweet.getUser().getScreen_name()));
            Picasso.with(context)
                    .load(tweet.getUser().getProfile_image_url())
                    .into(tweetView.tweetProfilePic);
        }

        tweetView.tweetBody.setText(tweet.getText());

        tweetView.btnTweetReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onTweetReplyClick(tweet);
            }
        });
        tweetView.btnTweetRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onTweetRetweetClick(tweet);
            }
        });
        tweetView.btnTweetHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onTweetHeartClick(tweet);
            }
        });
        tweetView.btnTweetMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onTweetMenuClick(tweet);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tweet_name)           TextView tweetName;
        @Bind(R.id.tweet_username)       TextView tweetUsername;
        @Bind(R.id.tweet_body)           TextView tweetBody;
        @Bind(R.id.tweet_profile_pic)    ImageView tweetProfilePic;
        @Bind(R.id.btn_tweet_reply)      ImageView btnTweetReply;
        @Bind(R.id.btn_tweet_retweet)    ImageView btnTweetRetweet;
        @Bind(R.id.btn_tweet_heart)      ImageView btnTweetHeart;
        @Bind(R.id.btn_tweet_menu)       ImageView btnTweetMenu;
        @Bind(R.id.tweet_ic_retweeted)   ImageView icRetweeted;
        @Bind(R.id.tweet_name_retweeted) TextView tweetRetweetedName;

        public TweetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
