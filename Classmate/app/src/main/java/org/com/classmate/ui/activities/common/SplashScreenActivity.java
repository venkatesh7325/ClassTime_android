package org.com.classmate.ui.activities.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import org.com.classmate.R;
import org.com.classmate.ui.activities.admin.AdminDashBoardActivity;
import org.com.classmate.ui.activities.students.StudentDashBoardActivity;
import org.com.classmate.ui.activities.teacher.TeachersDashboardActivity;
import org.com.classmate.utils.Utility;

public class SplashScreenActivity extends AppCompatActivity {
    /**
     * Duration of wait
     **/
    int SPLASH_DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Utility.getLoginID(SplashScreenActivity.this) > 0) {
                    navigateLogin();
                } else {
                    Intent mainIntent = new Intent(SplashScreenActivity.this, IntroductionActivity.class);
                    startActivity(mainIntent);
                    finish();
                }
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    void navigateLogin() {
        Intent mainIntent = null;
        switch (Utility.getRole(SplashScreenActivity.this)) {
            case "1":
                mainIntent = new Intent(SplashScreenActivity.this, AdminDashBoardActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            case "2":
                mainIntent = new Intent(SplashScreenActivity.this, TeachersDashboardActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            case "3":
                mainIntent = new Intent(SplashScreenActivity.this, TeachersDashboardActivity.class);
                startActivity(mainIntent);
                finish();
                break;
            case "4":
                mainIntent = new Intent(SplashScreenActivity.this, StudentDashBoardActivity.class);
                startActivity(mainIntent);
                finish();
                break;
        }
    }
}
