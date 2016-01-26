package be.kdg.twitterandroid.services;

import be.kdg.twitterandroid.domain.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Maarten on 5/01/2016.
 */
public interface UserService {
    @GET("account/verify_credentials.json")
    Call<User> getCurrentUser();

    @GET("users/show.json")
    Call<User> getUser(
            @Query("user_id") long userId
    );

    @POST("blocks/create.json")
    Call<User> blockUser(
            @Query("user_id") long userId
    );

    @POST("blocks/create.json")
    Call<User> blockUser(
            @Query("screen_name") String screenName
    );

    @POST("mutes/users/create.json")
    Call<User> muteUser(
            @Query("user_id") long userId
    );

    @POST("mutes/users/create.json")
    Call<User> muteUser(
            @Query("screen_name") String screenName
    );
}
