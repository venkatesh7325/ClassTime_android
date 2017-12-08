package org.com.classmate.utils;

/**
 * Created by mahiti on 8/12/15.
 */
public class Logger
{
    /**
     * Default constructors
     */
    private Logger(){
        // This is required
    }

    /**
     * function to use in catch block....
     * @param tag
     * @param desc
     * @param e
     */
    public static void logE(String tag, String desc, Exception e)
    {
        android.util.Log.e(tag,desc,e);
    }

    /**
     *  function to use for debug and showing in console..
     * @param tag
     * @param desc
     */
    public static void logD(String tag, String desc)
    {
        android.util.Log.d(tag,""+desc);
    }

    /**
     * function to use for debug and showing in console....
     * @param tag
     * @param desc
     */
    public static void logV(String tag, String desc)
    {
        android.util.Log.v(tag,""+desc);
    }

}
