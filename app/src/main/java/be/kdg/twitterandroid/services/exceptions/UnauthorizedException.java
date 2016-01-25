package be.kdg.twitterandroid.services.exceptions;

import java.io.IOException;

/**
 * Created by Maarten on 25/01/2016.
 */
public class UnauthorizedException extends IOException {

    public UnauthorizedException() {
        super("Request unauthorized: invalid token or signature");
    }
}
