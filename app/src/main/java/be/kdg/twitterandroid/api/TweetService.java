package be.kdg.twitterandroid.api;

import java.util.List;

import be.kdg.twitterandroid.domain.Tweet;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Maarten on 5/01/2016.
 */
public interface TweetService {
    @GET("/statuses/user_timeline.json")
    void getUserTimeline(
            @Query("screen_name") String screenName,
            @Query("count") int count,
            Callback<List<Tweet>> callback
    );

    @GET("/statuses/home_timeline.json")
    void getHomeTimeline(
            @Query("count") int count,
            @Query("max_id") Long max_id,
            Callback<List<Tweet>> callback
    );
}