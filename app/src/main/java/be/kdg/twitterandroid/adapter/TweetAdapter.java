package be.kdg.twitterandroid.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.activities.TweetInteractionListener;
import be.kdg.twitterandroid.domain.Entities;
import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
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
            tweetView.tweetRetweetedName.setTag(tweet.getUser());

            tweetView.tweetName.setText(tweet.getRetweeted_status().getUser().getName());
            tweetView.tweetName.setTag(tweet.getRetweeted_status().getUser());

            tweetView.tweetUsername.setText(String.format("@%s", tweet.getRetweeted_status().getUser().getScreen_name()));
            tweetView.tweetUsername.setTag(tweet.getRetweeted_status().getUser());

            tweetView.tweetProfilePic.setTag(tweet.getRetweeted_status().getUser());

            Picasso.with(context)
                    .load(tweet.getRetweeted_status().getUser().getProfile_image_url())
                    .into(tweetView.tweetProfilePic);

        } else {
            tweetView.icRetweeted.setVisibility(View.GONE);
            tweetView.tweetRetweetedName.setVisibility(View.GONE);

            tweetView.tweetName.setText(tweet.getUser().getName());
            tweetView.tweetName.setTag(tweet.getUser());

            tweetView.tweetUsername.setText(String.format("@%s", tweet.getUser().getScreen_name()));
            tweetView.tweetUsername.setTag(tweet.getUser());

            tweetView.tweetProfilePic.setTag(tweet.getUser());

            Picasso.with(context)
                    .load(tweet.getUser().getProfile_image_url())
                    .into(tweetView.tweetProfilePic);
        }

        Tweet displayedTweet = (tweet.getRetweeted_status() == null) ? tweet : tweet.getRetweeted_status();
        SpannableString tweetBody = new SpannableString(displayedTweet.getText());
        for(Entities.HashTagEntity hashtag : displayedTweet.getEntities().getHashtags()){
            tweetBody.setSpan(
                    new ForegroundColorSpan(Color.argb(255, 50, 50, 50)),
                    hashtag.getIndices()[0] + 1,
                    hashtag.getIndices()[1],
                    0
            );
        }
        for(Entities.UserMentionsEntity userMention : displayedTweet.getEntities().getUser_mentions()){
            tweetBody.setSpan(
                    new ForegroundColorSpan(Color.argb(255, 50, 50, 50)),
                    userMention.getIndices()[0] + 1,
                    userMention.getIndices()[1],
                    0
            );
        }
        for(final Entities.UrlEntity url : displayedTweet.getEntities().getUrls()){
            tweetBody.setSpan(
                    new ClickableSpan() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.getExpanded_url()));
                            context.startActivity(intent);
                        }
                    },
                    url.getIndices()[0],
                    url.getIndices()[1],
                    0
            );
        }
        tweetView.tweetBody.setMovementMethod(LinkMovementMethod.getInstance());
        tweetView.tweetBody.setText(tweetBody);

        tweetView.tweetTimestamp.setText(getSimpleDateString(tweet.getCreated_at()));

        if(tweet.getRetweet_count() > 0){
            tweetView.tweetRetweetCount.setText(String.valueOf(tweet.getRetweet_count()));
        } else {
            tweetView.tweetRetweetCount.setText("");
        }

        if(tweet.getFavorite_count() > 0){
            tweetView.tweetHeartCount.setText(String.valueOf(tweet.getFavorite_count()));
        } else {
            tweetView.tweetHeartCount.setText("");
        }

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
        tweetView.tweetUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onUserClick((User) view.getTag());
            }
        });
        tweetView.tweetName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onUserClick((User)view.getTag());
            }
        });
        tweetView.tweetRetweetedName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onUserClick((User)view.getTag());
            }
        });
        tweetView.tweetProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interactionListener.onUserClick((User)view.getTag());
            }
        });
    }

    private static String getSimpleDateString(String twitterDateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(twitterDateString);
            Date now = new Date();
            long delta = (now.getTime() - date.getTime()) / (1000 * 60 * 60); // delta in hours

            if(delta < 1)
            {
                long minuteDelta = (now.getTime() - date.getTime()) / (1000 * 60); // delta in minutes
                return minuteDelta + "m";
            }
            if(delta < 24)
                return delta + "h";
            if(delta > 168)
                return (delta / 168) + "wk";
            if(delta >= 24)
                return (delta / 24) + "d";

            return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    public static class TweetViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tweet_name)           TextView tweetName;
        @Bind(R.id.tweet_username)       TextView tweetUsername;
        @Bind(R.id.tweet_timestamp)      TextView tweetTimestamp;
        @Bind(R.id.tweet_body)           TextView tweetBody;
        @Bind(R.id.tweet_profile_pic)    ImageView tweetProfilePic;
        @Bind(R.id.btn_tweet_reply)      ImageView btnTweetReply;
        @Bind(R.id.btn_tweet_retweet)    LinearLayout btnTweetRetweet;
        @Bind(R.id.tweet_retweet_count)  TextView tweetRetweetCount;
        @Bind(R.id.btn_tweet_heart)      LinearLayout btnTweetHeart;
        @Bind(R.id.tweet_heart_count)    TextView tweetHeartCount;
        @Bind(R.id.btn_tweet_menu)       ImageView btnTweetMenu;
        @Bind(R.id.tweet_ic_retweeted)   ImageView icRetweeted;
        @Bind(R.id.tweet_name_retweeted) TextView tweetRetweetedName;

        public TweetViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
