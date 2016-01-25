package be.kdg.twitterandroid.services.exceptions;

import java.io.IOException;

/**
 * Created by Maarten on 25/01/2016.
 */
public class RateLimitExceededException extends IOException {

    public RateLimitExceededException() {
        super("Rate limit exceeded");
    }
}
