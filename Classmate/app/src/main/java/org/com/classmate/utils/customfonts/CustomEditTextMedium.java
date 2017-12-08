package org.com.classmate.utils.customfonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by cts on 6/22/17.
 */

public class CustomEditTextMedium extends EditText {

    /**
     * Parametrized constructors
     * @param context
     */
    public CustomEditTextMedium(Context context) {
        super(context);
        setFont();
    }

    /**
     *Parametrized constructors
     * @param context
     * @param attrs
     */
    public CustomEditTextMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }

    /**
     * Parametrized constructors
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomEditTextMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
