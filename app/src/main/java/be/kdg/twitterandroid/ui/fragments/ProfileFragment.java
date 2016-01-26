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
import be.kdg.twitterandroid.ui.activities.listeners.DefaultTweetInteractionHandler;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
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
        DefaultTweetInteractionHandler interactionHandler = new DefaultTweetInteractionHandler(
                application.getProfileTweets(),
                swipeRefreshLayout,
                getActivity()
        );
        tweetAdapter = new TweetAdapter(
                application.getProfileTweets(),
                interactionHandler,
                getActivity()
        );
        interactionHandler.setTweetAdapter(tweetAdapter);
        profileTimeline.setAdapter(tweetAdapter);
        LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
        profileTimeline.setLayoutManager(llmanager);
    }

    @Override
    public void onRefresh() {
        showUser(application.getCurrentUser());
        loadUserTweets(application.getCurrentUser().getId());
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void loadUserTweets(long userId) {
        TwitterServiceFactory.getTweetService().getUserTimeline(userId, 20).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Response<List<Tweet>> response) {
                application.getProfileTweets().clear();
                application.getProfileTweets().addAll(response.body());
                tweetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Snackbar.make(swipeRefreshLayout, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
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
        TwitterServiceFactory.getUserService().getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                showUser(response.body());
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(getView(), "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
