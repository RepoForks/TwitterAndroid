package be.kdg.twitterandroid.utils;

import android.text.format.DateUtils;

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
            long delta = (now.getTime() - date.getTime()) / (1000 * 60 * 60); // delta in hours

            if(delta < 1)
            {
                long minuteDelta = (now.getTime() - date.getTime()) / (1000 * 60); // delta in minutes
                return minuteDelta + "m";
            }
            if(delta < 24)
                return delta + "h";
            if(delta > 168)
                return (delta / 168) + "wk";
            if(delta >= 24)
                return (delta / 24) + "d";

            return DateUtils.getRelativeTimeSpanString(date.getTime()).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
