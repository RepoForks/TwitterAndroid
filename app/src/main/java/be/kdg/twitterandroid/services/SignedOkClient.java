package be.kdg.twitterandroid.services;

import java.io.IOException;
import java.net.HttpURLConnection;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpRequest;
import retrofit.client.OkClient;
import retrofit.client.Request;

/**
 * Created by Maarten on 14/01/2016.
 */
public class SignedOkClient extends OkClient {
    private static final String TAG = "SignedOkClient";
    private OAuthConsumer mConsumer = null;

    public SignedOkClient(OAuthConsumer consumer) {
        super();
        mConsumer = consumer;
    }

    @Override
    protected HttpURLConnection openConnection(Request request) throws IOException {
        HttpURLConnection connection = super.openConnection(request);
        try {
            HttpRequest signedReq = mConsumer.sign(connection);
        } catch (OAuthMessageSignerException | OAuthExpectationFailedException | OAuthCommunicationException e) {
            e.printStackTrace();
        }
        return connection;
    }
}