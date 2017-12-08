package org.com.classmate.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;

/**
 * Created by hp  pc on 24-06-2017.
 */

public class DynamicCustomButtons {
    public static void setbuttonFontLight(Button button, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Light.ttf");
        button.setTypeface(tf);
    }

    public static void setbuttonFontBold(Button button, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Bold.ttf");
        button.setTypeface(tf);
    }

    public static void setbuttonFontRegular(Button button, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Regular.ttf");
        button.setTypeface(tf);
    }

    public static void setbuttonFontSemiBold(Button button, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-SemiBold.ttf");
        button.setTypeface(tf);
    }
    public static void setbuttonFontThin(Button button, Context activity) {
        Typeface tf = Typeface.createFromAsset(
                activity.getAssets(), "fonts/JosefinSans-Thin.ttf");
        button.setTypeface(tf);
    }
}
