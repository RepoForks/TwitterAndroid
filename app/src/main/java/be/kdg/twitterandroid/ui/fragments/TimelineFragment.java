package be.kdg.twitterandroid.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.ui.activities.listeners.EndlessRecyclerOnScrollListener;
import be.kdg.twitterandroid.ui.activities.listeners.TweetInteractionListener;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class TimelineFragment extends Fragment implements TweetInteractionListener {
    @Bind(R.id.list_tweets)          RecyclerView listTweets;
    @Bind(R.id.swiperefresh_tweets)  SwipeRefreshLayout swipeRefreshLayout;

    private TwitterAndroidApplication application;
    private TweetAdapter tweetAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (TwitterAndroidApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);

        setupTimeline();
        setupRefreshLayout();

        if(application.getTweetService() != null) refreshTweets();

        return view;
    }

    private void setupTimeline(){
        tweetAdapter = new TweetAdapter(
                application.getTweets(), // Tweets
                this,                    // TweetInteractionListener
                getActivity()            // Activity
        );
        listTweets.setAdapter(tweetAdapter);

        LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
        listTweets.setLayoutManager(llmanager);
        listTweets.addOnScrollListener(new EndlessRecyclerOnScrollListener(llmanager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreTweets();
            }
        });
    }

    private void setupRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTweets();
            }
        });
    }

    public void refreshTweets(){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
            }
        });
        application.getTweetService().getHomeTimeline(20, null, new Callback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets, Response response) {
                List<Tweet> tweetList = application.getTweets();
                tweetList.clear();
                tweetList.addAll(tweets);
                TimelineFragment.this.tweetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(listTweets, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadMoreTweets(){
        application.getTweetService().getHomeTimeline(20, getLastTweetId() - 1, new Callback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets, Response response) {
                application.getTweets().addAll(tweets);
                TimelineFragment.this.tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(listTweets, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private long getLastTweetId(){
        return application.getTweets()
                .get( application.getTweets().size() - 1 )
                .getId();
    }

    @Override
    public void onTweetReplyClick(Tweet tweet) {
        Snackbar.make(listTweets, tweet.getId() + " reply", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTweetRetweetClick(Tweet tweet) {
        Snackbar.make(listTweets, tweet.getId() + " retweet", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTweetHeartClick(Tweet tweet) {
        Snackbar.make(listTweets, tweet.getId() + " heart", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onTweetMenuClick(Tweet tweet) {
        Snackbar.make(listTweets, tweet.getId() + " menu", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onUserClick(User user) {
        Snackbar.make(listTweets, user.getName() + " clicked", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onHashtagClick(String hashtag) {
        Snackbar.make(listTweets, "#" + hashtag + " clicked", Snackbar.LENGTH_SHORT).show();
    }
}
