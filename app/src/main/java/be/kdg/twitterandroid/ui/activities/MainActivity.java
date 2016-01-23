package be.kdg.twitterandroid.ui.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.ui.activities.listeners.EndlessRecyclerOnScrollListener;
import be.kdg.twitterandroid.ui.activities.listeners.TweetInteractionListener;
import be.kdg.twitterandroid.ui.adapters.TweetAdapter;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.domain.Tweet;
import be.kdg.twitterandroid.domain.User;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TweetInteractionListener {
    @Bind(R.id.toolbar)       Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout drawer;
    @Bind(R.id.nav_view)      NavigationView navigationView;
    @Bind(R.id.list_tweets)   RecyclerView listTweets;
    @Bind(R.id.swiperefresh_tweets) SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQ_AUTH = 0;

    private TwitterAndroidApplication application;
    private List<Tweet> tweets;
    private TweetAdapter tweetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        application = (TwitterAndroidApplication) getApplication();

        setupNavigationMenu();
        setupTweetList();
        setupRefreshLayout();

        if(!userHasAuthTokens()){
            Intent authIntent = new Intent(this, TwitterAuthActivity.class);
            startActivityForResult(authIntent, REQ_AUTH);
        } else {
            TwitterServiceFactory twitterServiceFactory = new TwitterServiceFactory();
            twitterServiceFactory.setOAuthTokensFromSharedPreferences(getApplication());
            ((TwitterAndroidApplication)getApplication()).setTweetService(twitterServiceFactory.getTweetService());
            refreshTweets();
        }
    }

    private void setupTweetList(){
        tweets = new ArrayList<>();
        tweetAdapter = new TweetAdapter(
                tweets, // Tweets
                this,   // TweetInteractionListener
                this    // Activity
        );
        listTweets.setAdapter(tweetAdapter);

        LinearLayoutManager llmanager = new LinearLayoutManager(this);
        listTweets.setLayoutManager(llmanager);
        listTweets.addOnScrollListener(new EndlessRecyclerOnScrollListener(llmanager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMoreTweets();
            }
        });
    }

    private void setupNavigationMenu(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTweets();
            }
        });
    }

    private boolean userHasAuthTokens(){
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplication());
        return sharedPrefs.contains("token") && sharedPrefs.contains("tokenSecret");
    }

    public void refreshTweets(){
        swipeRefreshLayout.setRefreshing(true);
        application.getTweetService().getHomeTimeline(20, null, new Callback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets, Response response) {
                MainActivity.this.tweets.clear();
                MainActivity.this.tweets.addAll(tweets);
                MainActivity.this.tweetAdapter.notifyDataSetChanged();
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
        application.getTweetService().getHomeTimeline(20, tweets.get(tweets.size() - 1).getId() - 1, new Callback<List<Tweet>>() {
            @Override
            public void success(List<Tweet> tweets, Response response) {
                MainActivity.this.tweets.addAll(tweets);
                MainActivity.this.tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(listTweets, "Error: " + error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQ_AUTH:
                refreshTweets();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_1) {

        } else if (id == R.id.nav_2) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
