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

   /* public static String getYearId(String year) {
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
    }*/

    public static String getDay(String days) {
        String day = "";
        switch (days) {
            case "1":
                day = "Monday";
                break;
            case "2":
                day = "Tuesday";
                break;
            case "3":
                day = "Wednesday";
                break;
            case "4":
                day = "Thursday";
                break;
            case "5":
                day = "Friday";
                break;
            case "6":
                day = "Saturday";
                break;
        }
        return day;
    }

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

    public static int setPeriod(int position) {
        int day = 0;
        if (position <= 8 || position <= 13)
            return 1;
        if (position <= 15 || position <= 20)
            return 2;
        if (position <= 22 || position <= 27)
            return 3;
        if (position <= 29 || position <= 34)
            return 4;
        if (position <= 36 || position <= 41)
            return 5;
        if (position <= 43 || position <= 48)
            return 6;
        if (position <= 50 || position <= 55)
            return 7;
        return day;

    }

    public static int setDay(int position) {
        int period = 0;
        if (position <= 8 || position <= 13) {

            for (int j = 8; j <= 13; j++) {
                period++;
                if (position == j)
                    return period;
            }

        }
        if (position <= 15 || position <= 20) {
            for (int j = 15; j <= 20; j++) {
                period++;
                if (position == j)
                    return period;

            }
        }
        if (position <= 22 || position <= 27) {
            for (int j = 22; j <= 27; j++) {
                period++;
                if (position == j)
                    return period;

            }
        }
        if (position <= 29 || position <= 34) {
            for (int j = 29; j <= 34; j++) {
                period++;
                if (position == j)
                    return period;

            }
        }
        if (position <= 36 || position <= 41) {
            for (int j = 36; j <= 41; j++) {
                period++;
                if (position == j)
                    return period;

            }
        }
        if (position <= 43 || position <= 48) {
            for (int j = 43; j <= 48; j++) {
                period++;
                if (position == j)
                    return period;

            }
        }
        if (position <= 50 || position <= 55) {
            for (int j = 50; j <= 55; j++) {
                period++;
                if (position == j)
                    return period;
            }
        }

        return period;
    }

}