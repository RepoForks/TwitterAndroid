package be.kdg.twitterandroid.services;

import be.kdg.twitterandroid.domain.User;
import retrofit2.Call;
import retrofit2.http.GET;
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
}
