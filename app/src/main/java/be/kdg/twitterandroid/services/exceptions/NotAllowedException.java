package be.kdg.twitterandroid.services.exceptions;

import java.io.IOException;

/**
 * Created by Maarten on 25/01/2016.
 */
public class NotAllowedException extends IOException {
    public NotAllowedException() {
        super("Action is not allowed for this status");
    }
}
