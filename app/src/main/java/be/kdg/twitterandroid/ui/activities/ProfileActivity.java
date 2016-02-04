package be.kdg.twitterandroid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.ui.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {
    private long userId = -1;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        profileFragment = new ProfileFragment();

        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getExtras().containsKey("userId")){
            this.userId = intent.getLongExtra("userId", -1);
            profileFragment.setUserId(userId);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.framelayout_profile, profileFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
