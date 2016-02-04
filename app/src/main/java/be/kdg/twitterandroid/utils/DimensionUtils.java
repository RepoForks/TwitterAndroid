package be.kdg.twitterandroid.utils;

import android.content.res.Resources;

/**
 * Created by Maarten on 4/02/2016.
 */
public final class DimensionUtils {

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}
