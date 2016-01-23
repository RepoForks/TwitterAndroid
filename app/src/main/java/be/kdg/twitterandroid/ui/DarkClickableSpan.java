package be.kdg.twitterandroid.ui;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Maarten on 23/01/2016.
 */
public abstract class DarkClickableSpan extends ClickableSpan {
    @Override
    public abstract void onClick(View view);

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
        ds.setColor(Color.argb(255, 50, 50, 50));
    }
}
