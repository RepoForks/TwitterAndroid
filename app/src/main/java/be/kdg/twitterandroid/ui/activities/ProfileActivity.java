package be.kdg.twitterandroid.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.ui.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {
    private long userId = -1;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}
