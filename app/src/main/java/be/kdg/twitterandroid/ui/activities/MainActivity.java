package be.kdg.twitterandroid.ui.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.domain.User;
import be.kdg.twitterandroid.services.TwitterServiceFactory;
import be.kdg.twitterandroid.services.UserService;
import be.kdg.twitterandroid.ui.fragments.AboutFragment;
import be.kdg.twitterandroid.ui.fragments.ProfileFragment;
import be.kdg.twitterandroid.ui.fragments.TimelineFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.toolbar)          Toolbar toolbar;
    @Bind(R.id.drawer_layout)    DrawerLayout drawer;
    @Bind(R.id.nav_view)         NavigationView navigationView;
    @Bind(R.id.framelayout_main) FrameLayout frameLayout;

    TextView txtHeaderScreenName;
    TextView txtHeaderName;
    CircleImageView imgHeaderProfilePic;
    LinearLayout navHeaderBg;

    private static final int REQ_AUTH = 0;

    private TwitterAndroidApplication application;
    private TimelineFragment timelineFragment;
    private ProfileFragment profileFragment;
    private AboutFragment aboutFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        application = (TwitterAndroidApplication) getApplication();

        setupFragments();
        setupNavigationMenu();

        if(!application.userHasAuthTokens()){
            Intent authIntent = new Intent(this, TwitterAuthActivity.class);
            startActivityForResult(authIntent, REQ_AUTH);
        } else {
            fetchCurrentUser();
        }
    }

    private void fetchCurrentUser() {
        UserService userService = TwitterServiceFactory.getUserService();
        userService.getCurrentUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Response<User> response) {
                if(!response.isSuccess()){
                    Snackbar.make(frameLayout, "Error: " + response.errorBody().toString(), Snackbar.LENGTH_LONG).show();
                    return;
                }

                User user = response.body();

                application.setCurrentUser(user);

                txtHeaderScreenName.setText("@" + user.getScreen_name());
                txtHeaderName.setText(user.getName());

                Picasso.with(MainActivity.this)
                        .load(user.getProfile_image_url())
                        .into(imgHeaderProfilePic);

                final LinearLayout finalheaderbg = navHeaderBg;
                final Target headerBgTarget = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        finalheaderbg.setBackground(new BitmapDrawable(getResources(), bitmap));
                        System.out.println("Bitmap loaded???");
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        System.out.println("Bitmap failed");
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        System.out.println("Prepare load");
                    }
                };
                finalheaderbg.setTag(headerBgTarget);

                Picasso.with(MainActivity.this)
                        .load(user.getProfile_banner_url())
                        .into(headerBgTarget);
            }

            @Override
            public void onFailure(Throwable t) {
                Snackbar.make(frameLayout, "Error: " + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setupFragments(){
        timelineFragment = new TimelineFragment();
        profileFragment = new ProfileFragment();
        aboutFragment = new AboutFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.framelayout_main, timelineFragment).commit();

        setTitle(navigationView.getMenu().getItem(1).getTitle());
        navigationView.getMenu().getItem(1).setChecked(true);
    }

    private void setupNavigationMenu(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        txtHeaderScreenName = (TextView) navHeader.findViewById(R.id.nav_header_screenname);
        txtHeaderName = (TextView) navHeader.findViewById(R.id.nav_header_name);
        imgHeaderProfilePic = (CircleImageView) navHeader.findViewById(R.id.nav_header_profilepic);
        navHeaderBg = (LinearLayout) navHeader.findViewById(R.id.nav_header_bg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;

        switch (requestCode) {
            case REQ_AUTH:
                timelineFragment.refreshTweets();
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = timelineFragment;
        switch(id){
            case R.id.nav_profile:
                fragment = profileFragment;
                break;

            case R.id.nav_timeline:
                fragment = timelineFragment;
                break;

            case R.id.nav_about:
                fragment = aboutFragment;
                break;
        }
        fragmentManager.beginTransaction().replace(R.id.framelayout_main, fragment).commit();

        item.setChecked(true);
        setTitle(item.getTitle());
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
