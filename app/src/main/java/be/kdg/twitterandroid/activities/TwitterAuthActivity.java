package be.kdg.twitterandroid.activities;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import be.kdg.twitterandroid.R;
import be.kdg.twitterandroid.TwitterAndroidApplication;
import be.kdg.twitterandroid.api.TwitterServiceFactory;
import be.kdg.twitterandroid.config.Constants;
import butterknife.Bind;
import butterknife.ButterKnife;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

public class TwitterAuthActivity extends AppCompatActivity {
    private final static String CALLBACK = "oauth://twitter";

    private String mAuthUrl;
    private OAuthProvider mProvider;
    private OAuthConsumer mConsumer;
    private SharedPreferences sharedPreferences;

    @Bind(R.id.web_view) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_auth);
        ButterKnife.bind(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("oauth")) {
                    Uri uri = Uri.parse(url);
                    onOAuthCallback(uri);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        initTwitter();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplication());
    }

    private void initTwitter(){
        mConsumer = new DefaultOAuthConsumer(
                Constants.API_KEY,
                Constants.API_SECRET
        );
        mProvider = new DefaultOAuthProvider(
                Constants.API_OAUTH_REQ_TOKEN,
                Constants.API_OAUTH_ACC_TOKEN,
                Constants.API_OAUTH_AUTH
        );

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    mAuthUrl = mProvider.retrieveRequestToken(mConsumer, CALLBACK);
                } catch (OAuthMessageSignerException | OAuthNotAuthorizedException | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                if (!TextUtils.isEmpty(mAuthUrl)) {
                    webView.loadUrl(mAuthUrl);
                }
            }
        }.execute();
    }

    private void onOAuthCallback(final Uri uri) {
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                String oauth_verifier = uri.getQueryParameter("oauth_verifier");
                try {
                    mProvider.retrieveAccessToken(mConsumer, oauth_verifier);

                    String token = mConsumer.getToken();
                    String tokenSecret = mConsumer.getTokenSecret();

                    TwitterServiceFactory twitterServiceFactory = new TwitterServiceFactory();
                    twitterServiceFactory.setOAuthTokens(token, tokenSecret);
                    ((TwitterAndroidApplication)getApplication()).setTweetService(twitterServiceFactory.getTweetService());

                    SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                    prefsEditor.putString("token", token);
                    prefsEditor.putString("tokenSecret", tokenSecret);
                    prefsEditor.apply();

                } catch (OAuthMessageSignerException | OAuthNotAuthorizedException | OAuthExpectationFailedException | OAuthCommunicationException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setResult(RESULT_OK);
                finish();
            }
        }.execute();

    }
}
