package be.kdg.twitterandroid.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.ui.activities.listeners.TweetInteractionListener;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileFragment extends Fragment implements TweetInteractionListener, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.profile_banner)      RelativeLayout profileBanner;
    @Bind(R.id.profile_picture)     CircleImageView profilePicture;
    @Bind(R.id.profile_name)        TextView name;
    @Bind(R.id.profile_screenname)  TextView screenName;
    @Bind(R.id.profile_tweets)      TextView tweets;
    @Bind(R.id.profile_following)   TextView following;
    @Bind(R.id.profile_followers)   TextView followers;
    @Bind(R.id.profile_timeline)    RecyclerView profileTimeline;
    @Bind(R.id.swiperefresh_profile)  SwipeRefreshLayout swipeRefreshLayout;

    private TwitterAndroidApplication application;
    private TweetAdapter tweetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        setupRecyclerView();
        setupRefreshLayout();
        onRefresh();

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (TwitterAndroidApplication) getActivity().getApplication();
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setupRecyclerView(){
        tweetAdapter = new TweetAdapter(application.getProfileTweets(), this, getActivity());
        profileTimeline.setAdapter(tweetAdapter);
        LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
        profileTimeline.setLayoutManager(llmanager);
    }

    @Override
    public void onRefresh() {
        loadUserTweets(application.getCurrentUser().getId());
        showUser(application.getCurrentUser());
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void loadUserTweets(long userId) {
        TwitterServiceFactory.getTweetService().getUserTimeline(userId, 20, new Callback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets, Response response) {
                application.getProfileTweets().clear();
                application.getProfileTweets().addAll(tweets);
                tweetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void showUser(User user){
        name.setText(user.getName());
        screenName.setText("@" + user.getScreen_name());
        tweets.setText(String.valueOf(user.getStatuses_count()));
        following.setText(String.valueOf(user.getFriends_count()));
        followers.setText(String.valueOf(user.getFollowers_count()));

        Picasso.with(getActivity())
                .load(user.getProfile_image_url())
                .into(profilePicture);

        final RelativeLayout finalProfileBanner = profileBanner;
        final Target bannerTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                finalProfileBanner.setBackground(new BitmapDrawable(getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) { }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) { }
        };

        Picasso.with(getActivity())
                .load(user.getProfile_banner_url())
                .into(bannerTarget);
    }

    public void fetchUser(long userId){
        TwitterServiceFactory.getUserService().getUser(userId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                showUser(user);
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(getView(), "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onTweetReplyClick(Tweet tweet) {

    }

    @Override
    public void onTweetRetweetClick(Tweet tweet) {

    }

    @Override
    public void onTweetHeartClick(Tweet tweet) {

    }

    @Override
    public void onTweetMenuClick(Tweet tweet) {

    }

    @Override
    public void onUserClick(User user) {

    }

    @Override
    public void onHashtagClick(String hashtag) {

    }
}
