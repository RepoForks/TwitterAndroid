package be.kdg.twitterandroid.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import be.kdg.twitterandroid.config.Constants;
import be.kdg.twitterandroid.services.exceptions.NotAllowedException;
import be.kdg.twitterandroid.services.exceptions.NotFoundException;
import be.kdg.twitterandroid.services.exceptions.RateLimitExceededException;
import be.kdg.twitterandroid.services.exceptions.UnauthorizedException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

/**
 * Created by Maarten on 14/01/2016.
 */
public class TwitterServiceFactory {
    private static TweetService tweetService;
    private static UserService userService;
    private static Retrofit retrofit;

    private TwitterServiceFactory() {
    }

    public static void setOAuthTokens(String token, String tokenSecret) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(
                Constants.API_KEY,
                Constants.API_SECRET
        );
        consumer.setTokenWithSecret(token, tokenSecret);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        Response response = chain.proceed(request);
                        System.out.println(response.code());
                        switch (response.code()) {
                            case 328:
                            case 403:
                                throw new NotAllowedException();
                            case 401:
                                throw new UnauthorizedException();
                            case 404:
                                throw new NotFoundException();
                            case 429:
                                throw new RateLimitExceededException();
                        }
                        return response;
                    }
                })
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static void setOAuthTokensFromSharedPreferences(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        setOAuthTokens(prefs.getString("token", null), prefs.getString("tokenSecret", null));
    }

    public static synchronized TweetService getTweetService() {
        if (tweetService == null) tweetService = retrofit.create(TweetService.class);
        return tweetService;
    }

    public static synchronized UserService getUserService() {
        if (userService == null) userService = retrofit.create(UserService.class);
        return userService;
    }
}
