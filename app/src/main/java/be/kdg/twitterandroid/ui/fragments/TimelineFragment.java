package be.kdg.twitterandroid.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.ui.activities.listeners.DefaultTweetInteractionHandler;
import be.kdg.twitterandroid.ui.activities.MainActivity;
import be.kdg.twitterandroid.ui.activities.listeners.EndlessRecyclerOnScrollListener;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class TimelineFragment extends Fragment {
    @Bind(R.id.list_tweets)          RecyclerView listTweets;
    @Bind(R.id.swiperefresh_tweets)  SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.fab)                  FloatingActionButton fab;

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
        setupFab();
        setupRefreshLayout();

        if(application.userHasAuthTokens()) refreshTweets();
        return view;
    }

    private void setupFab(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });
    }

    private void onFabClick() {
        ((MainActivity)getActivity()).showCreateTweetPopup();
    }

    private void setupTimeline(){
        DefaultTweetInteractionHandler interactionHandler = new DefaultTweetInteractionHandler(
                application.getTweets(),
                swipeRefreshLayout,
                getActivity()
        );
        tweetAdapter = new TweetAdapter(
                application.getTweets(), // Tweets
                interactionHandler,      // InteractionHandler
                getActivity()            // Activity
        );
        interactionHandler.setTweetAdapter(tweetAdapter);
        listTweets.setAdapter(tweetAdapter);

        LinearLayoutManager llmanager = new LinearLayoutManager(getActivity());
        listTweets.setLayoutManager(llmanager);
        listTweets.addOnScrollListener(new EndlessRecyclerOnScrollListener(llmanager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreTweets();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
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

        TwitterServiceFactory.getTweetService().getHomeTimeline(20, null).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Response<List<Tweet>> response) {
                List<Tweet> tweets = response.body();
                List<Tweet> tweetList = application.getTweets();
                tweetList.clear();
                tweetList.addAll(tweets);
                TimelineFragment.this.tweetAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(listTweets, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void loadMoreTweets(){
        TwitterServiceFactory.getTweetService().getHomeTimeline(20, getLastTweetId() - 1).enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Response<List<Tweet>> response) {
                application.getTweets().addAll(response.body());
                TimelineFragment.this.tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(listTweets, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private long getLastTweetId(){
        return application.getTweets()
                .get(application.getTweets().size() - 1)
                .getId();
    }
}
