package be.kdg.twitterandroid.ui.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.domain.SearchResponse;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.ui.activities.listeners.DefaultTweetInteractionHandler;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class HashtagActivity extends AppCompatActivity {
    @Bind(R.id.list_tweets)         RecyclerView listTweets;
    @Bind(R.id.swiperefresh_tweets) SwipeRefreshLayout swipeRefreshLayout;

    private TwitterAndroidApplication application;
    private TweetAdapter tweetAdapter;
    private String hashtag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        application = (TwitterAndroidApplication) getApplication();
        hashtag = getIntent().getStringExtra("hashtag");

        setTitle("#" + hashtag);

        setupTweetList();
        setupRefreshLayout();

        getTweets(hashtag);
    }

    private void setupRefreshLayout() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTweets(hashtag);
                swipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void setupTweetList() {
        DefaultTweetInteractionHandler interactionHandler = new DefaultTweetInteractionHandler(
                application.getHashtagTweets(),
                swipeRefreshLayout,
                this
        );
        tweetAdapter = new TweetAdapter(
                application.getHashtagTweets(), // Tweets
                interactionHandler,      // InteractionHandler
                this                     // Activity
        );
        interactionHandler.setTweetAdapter(tweetAdapter);
        listTweets.setAdapter(tweetAdapter);

        LinearLayoutManager llmanager = new LinearLayoutManager(this);
        listTweets.setLayoutManager(llmanager);
    }

    private void getTweets(String hashtag) {
        TwitterServiceFactory.getTweetService().searchTweets("#" + hashtag, 50).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Response<SearchResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.isSuccess()){
                    application.getHashtagTweets().clear();
                    application.getHashtagTweets().addAll(response.body().getStatuses());
                    tweetAdapter.notifyDataSetChanged();
                } else {
                    Snackbar.make(swipeRefreshLayout, "Error retrieving tweets", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(swipeRefreshLayout, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
