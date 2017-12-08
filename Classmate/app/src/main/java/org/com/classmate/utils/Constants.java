package org.com.classmate.utils;

/**
 * Created by hp  pc on 13-05-2017.
 */

public class Constants {

    public static final String CLASSTIME_PREF = "ADMIN_PREF";
    public static final String MAIN_URL = "";
    public static final String no_internet_connection = "No Internet";
    public static final String something_went_wrong = "Some thing went wrong";
    public static final String LOGIN_VERIFIED = "login_verify";
    public static final String ADMIN_ROLE = "1";
    public static final String HOD_ROLE = "2";
    public static final String TEACHER_ROLE = "3";
    public static final String STUDENT_ROLE = "4";

    public static String getYearId(String year) {
        String yearID = "";
        switch (year) {
            case "1 Year":
                yearID = "1";
                break;
            case "2nd Year":
                yearID = "2";
                break;
            case "3rd Year":
                yearID = "3";
                break;
            case "4th Year":
                yearID = "4";
                break;
            case "I":
                yearID = "1";
                break;
            case "II":
                yearID = "2";
                break;
        }
        return yearID;
    }

}
