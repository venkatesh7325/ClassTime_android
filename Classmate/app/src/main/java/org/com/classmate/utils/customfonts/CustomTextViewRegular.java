package org.com.classmate.utils.customfonts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by mahiti on 30/5/17.
 */

@SuppressLint("AppCompatCustomView")
public class CustomTextViewRegular extends TextView {

    /**
     * Parametrized constructors
     * @param context
     */
    public CustomTextViewRegular(Context context) {
        super(context);
        setFont();
    }

    /**
     *Parametrized constructors
     * @param context
     * @param attrs
     */
    public CustomTextViewRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    /**
     * Parametrized constructors
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTextViewRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
