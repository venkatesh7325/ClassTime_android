package org.com.classmate.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * Created by hp  pc on 03-06-2017.
 */

public class DynamicCustomTextView {
    public static void setTextViewFontLight(TextView textView, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Light.ttf");
        textView.setTypeface(tf);
    }

    public static void setTextViewFontBold(TextView textView, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Bold.ttf");
        textView.setTypeface(tf);
    }

    public static void setTextViewFontRegular(TextView textView, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Regular.ttf");
        textView.setTypeface(tf);
    }

    public static void setTextViewFontSemiBold(TextView textView, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        textView.setTypeface(tf);
    }
    public static void setTextViewFontThin(TextView textView, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Thin.ttf");
        textView.setTypeface(tf);
    }
}
