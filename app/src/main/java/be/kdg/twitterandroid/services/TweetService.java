package be.kdg.twitterandroid.services;

import java.util.List;

import be.kdg.twitterandroid.domain.Tweet;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Maarten on 5/01/2016.
 */
public interface TweetService {
    @GET("statuses/user_timeline.json")
    Call<List<Tweet>> getUserTimeline(
            @Query("screen_name") String screenName,
            @Query("count") int count
    );

    @GET("statuses/user_timeline.json")
    Call<List<Tweet>> getUserTimeline(
            @Query("user_id") long userId,
            @Query("count") int count
    );

    @GET("statuses/home_timeline.json")
    Call<List<Tweet>> getHomeTimeline(
            @Query("count") int count,
            @Query("max_id") Long max_id
    );

    @POST("statuses/retweet/{id}.json")
    Call<Tweet> retweetTweet(
            @Path("id") long tweetId
    );

    @POST("statuses/unretweet/{id}.json")
    Call<Tweet> unretweetTweet(
            @Path("id") long tweetId
    );

    @POST("favorites/create.json")
    Call<Tweet> favoriteTweet(
            @Query("id") long tweetId
    );

    @POST("favorites/destroy.json")
    Call<Tweet> unfavoriteTweet(
            @Query("id") long tweetId
    );

    @FormUrlEncoded
    @POST("statuses/update.json")
    Call<Tweet> tweet(
            @Field("status") String status
    );

    @FormUrlEncoded
    @POST("statuses/update.json")
    Call<Tweet> tweet(
            @Field("status") String status,
            @Query("in_reply_to_status_id") String in_reply_to_status_id
    );

    @POST("statuses/destroy/{id}.json")
    Call<Tweet> deleteTweet(
            @Path("id") long tweetId
    );
}
