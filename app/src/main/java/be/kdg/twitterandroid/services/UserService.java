package be.kdg.twitterandroid.services;

import be.kdg.twitterandroid.domain.User;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by Maarten on 5/01/2016.
 */
public interface UserService {
    @GET("/account/verify_credentials.json")
    void getCurrentUser(Callback<User> callback);
}
