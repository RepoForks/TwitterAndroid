package be.kdg.twitterandroid.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Maarten on 23/01/2016.
 */
public final class DateHelper {

    private DateHelper(){}

    public static String getSimpleDateString(String twitterDateString){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(twitterDateString);
            Date now = new Date();
            long delta = (now.getTime() - date.getTime()) / (1000 * 60); // delta in minutes

            if(delta < 1) return "now";
            else if(delta > 1 && delta < 60) return delta + "m";
            else if(delta > 60 && delta < 24 * 60) return delta / 60 + "h";
            else if(delta > 24 * 60 && delta < 24 * 60 * 7) return delta / 60 / 24 + "d";
            else return delta / 60 / 24 / 7 + "wk";

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
