package org.com.classmate.ui.activities.common;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.com.classmate.R;
import org.com.classmate.ui.activities.admin.AdminFormActivity;
import org.com.classmate.ui.activities.hod.HodFormActivity;
import org.com.classmate.ui.activities.students.StudentFormActivity;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

public class ChooseRoleActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ChooseRoleActivity.class.getSimpleName();
    Button btnAdmin, btnHod, btnTeacher, btnStudent;
    ImageView imgAdmin, imgHod, imgTeacher, imgStudent;
    String selectedRole = "";
    Button btnSignUp;
    CustomTextViewMedium btnSignIn;
    String logOrReg;
    LinearLayout llChooseRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_role);
        init();
    }

    void init() {
        llChooseRole = (LinearLayout) findViewById(R.id.ll_choose_root);
        btnAdmin = (Button) findViewById(R.id.img_admin);
        btnHod = (Button) findViewById(R.id.img_hod);
        btnTeacher = (Button) findViewById(R.id.img_teacher);
        btnStudent = (Button) findViewById(R.id.img_student);

        btnAdmin.setOnClickListener(this);
        btnHod.setOnClickListener(this);
        btnTeacher.setOnClickListener(this);
        btnStudent.setOnClickListener(this);

        imgAdmin = (ImageView) findViewById(R.id.img_admin_tick);
        imgHod = (ImageView) findViewById(R.id.img_hod_tick);
        imgTeacher = (ImageView) findViewById(R.id.img_teacher_tick);
        imgStudent = (ImageView) findViewById(R.id.img_student_tick);

        btnSignIn = (CustomTextViewMedium) findViewById(R.id.btn_signin);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_admin:
                showTickMark(imgAdmin, imgHod, imgTeacher, imgStudent);
                selectedRole = getResources().getString(R.string.admin);
                break;
            case R.id.img_hod:
                showTickMark(imgHod, imgAdmin, imgTeacher, imgStudent);
                selectedRole = getResources().getString(R.string.hod);
                break;
            case R.id.img_teacher:
                showTickMark(imgTeacher, imgHod, imgAdmin, imgStudent);
                selectedRole = getResources().getString(R.string.faculty);
                break;
            case R.id.img_student:
                showTickMark(imgStudent, imgHod, imgTeacher, imgAdmin);
                selectedRole = getResources().getString(R.string.student);
                break;
            case R.id.btn_signin:
                logOrReg = getResources().getString(R.string.sign_in);
                if (selectedRole.isEmpty()) {
                    Snackbar.make(llChooseRole, "Please Choose Your Role!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                modeOfLogin();
                break;
            case R.id.btn_signup:
                logOrReg = "Sign up";
                if (selectedRole.isEmpty()) {
                    Snackbar.make(llChooseRole, "Please Choose Your Role!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                navigateToNextScreen(selectedRole);
                break;
            default:
                break;
        }
    }

    void navigateToNextScreen(String selectedRb) {
        Intent i = null;
        Log.d(TAG, "Selected mode--" + selectedRb);
        Bundle b = new Bundle();
        b.putString("mode_of_login", selectedRb);
        switch (selectedRb) {
            case "Admin":
                i = new Intent(this, AdminFormActivity.class);
                i.putExtras(b);
                break;
            case "HOD":
                i = new Intent(this, HodFormActivity.class);
                i.putExtras(b);
                break;
            case "Teacher":
                i = new Intent(this, HodFormActivity.class);
                i.putExtras(b);
                break;
            case "Student":
                i = new Intent(this, StudentFormActivity.class);
                i.putExtras(b);
                break;
        }
        if (null != i)
            startActivity(i);
        finish();

    }

    private void modeOfLogin() {
        Log.d(TAG, "Selected mode--" + selectedRole);
        Bundle b = new Bundle();
        b.putString("mode_of_login", selectedRole);
        Intent i = new Intent(ChooseRoleActivity.this, LoginActivity.class);
        i.putExtras(b);
        startActivity(i);
        finish();
    }

    private void showTickMark(ImageView imageView1, ImageView imageView2, ImageView imageView3, ImageView imageView4) {
        imageView1.setVisibility(View.VISIBLE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        imageView4.setVisibility(View.GONE);
    }
}
