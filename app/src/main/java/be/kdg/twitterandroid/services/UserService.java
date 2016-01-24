package be.kdg.twitterandroid.services;

import be.kdg.twitterandroid.domain.User;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Maarten on 5/01/2016.
 */
public interface UserService {
    @GET("/account/verify_credentials.json")
    void getCurrentUser(Callback<User> callback);

    @GET("/users/show.json")
    void getUser(
        @Query("user_id") long userId,
        Callback<User> callback
    );
}
