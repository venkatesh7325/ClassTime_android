package org.com.classmate.ui.activities.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;

import com.google.gson.Gson;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.LoginResponseModel.LoginResponsePojo;
import org.com.classmate.ui.activities.admin.AdminDashBoardActivity;
import org.com.classmate.ui.activities.students.StudentDashBoardActivity;
import org.com.classmate.ui.activities.teacher.TeachersDashboardActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.DynamicCustomButtons;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    @Bind(R.id.edt_login_email)
    CustomEditTextMedium edtEmail;
    @Bind(R.id.edt_login_password)
    CustomEditTextMedium edtPassword;
    @Bind(R.id.btn_login)
    Button btnLogin;
    private String selectionMode = "";
    private String role = "";
    @Bind(R.id.tv_loginWithMobile)
    CustomTextViewMedium tvLoginWithMobile;
    @Bind(R.id.tv_loginWithEmail)
    CustomTextViewMedium tvLoginWithEmail;
    String loginWith = "mobile";
    boolean loginFlag = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getDateFromPreviousActivity();
        DynamicCustomButtons.setbuttonFontSemiBold(btnLogin, this);
    }

    private void getDateFromPreviousActivity() {
        Bundle b = getIntent().getExtras();
        if (b != null) {
            selectionMode = b.getString("mode_of_login");
            Log.d(TAG, "Mode--" + selectionMode);
            setLoginRole(selectionMode);
        }
    }

    void setLoginRole(String mode) {
        switch (mode) {
            case "Student":
                role = Constants.STUDENT_ROLE;
                break;
            case "Admin":
                role = Constants.ADMIN_ROLE;
                break;
            case "HOD":
                role = Constants.HOD_ROLE;
                break;
            case "Teacher":
                role = Constants.TEACHER_ROLE;
                break;
            default:
                break;
        }
    }

    @OnClick(R.id.btn_login)
    public void onclickLogin() {
        if (Utility.isConnectingToInternet(this))
            validateLoginFielsds();
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);
    }

    @OnClick(R.id.tv_loginWithMobile)
    public void onClickMobile() {
        if (!loginFlag) {
            loginFlag = true;
            loginWith = "mobile";
            edtEmail.setInputType(InputType.TYPE_CLASS_NUMBER);
            edtEmail.setHint("Mobile");
            tvLoginWithMobile.setBackground(getResources().getDrawable(R.color.dash_board_gray));
            tvLoginWithEmail.setBackground(getResources().getDrawable(R.color.app_white_buttons_bg_color));
        }

    }

    @OnClick(R.id.tv_loginWithEmail)
    public void onClickEmail() {
        if (loginFlag) {
            loginFlag = false;
            loginWith = "email";
            edtEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            edtEmail.setHint("Email");
            tvLoginWithEmail.setBackground(getResources().getDrawable(R.color.dash_board_gray));
            tvLoginWithMobile.setBackground(getResources().getDrawable(R.color.app_white_buttons_bg_color));
        }

    }


    void validateLoginFielsds() {
        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            ToastUtils.displayToast("Please enter email", this);
            return;
        }
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            ToastUtils.displayToast("Please enter password", this);
            return;
        }
        //ToastUtils.displayToast("Success", this);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            // hashMap.put("name", clzName);
            hashMap.put(loginWith, edtEmail.getText().toString().trim());
            hashMap.put("password", edtPassword.getText().toString().trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Admin Activity HASHAMP--" + hashMap);
        new APIRequest(this).postStringRequest(ApiConstants.LOGIN, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Activity--" + response);
                Gson gson = new Gson();
                LoginResponsePojo successResponsePojo = gson.fromJson(response, LoginResponsePojo.class);
                if (successResponsePojo.getMessage().equalsIgnoreCase("success")) {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                    Utility.saveUserID(LoginActivity.this, successResponsePojo.getId()); // saving user ID into pref
                    moveToDashboard(selectionMode);

                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                }

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, LoginActivity.this);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }

    private void moveToDashboard(String s) {
        Intent i = null;
        if (s.toLowerCase().equalsIgnoreCase(getResources().getString(R.string.student).toLowerCase())) {
            Utility.saveRole(LoginActivity.this, Constants.STUDENT_ROLE);
            i = new Intent(LoginActivity.this, StudentDashBoardActivity.class);
            startActivity(i);
            finish();
        } else if (s.toLowerCase().equalsIgnoreCase(getResources().getString(R.string.admin).toLowerCase())) {
            Utility.saveRole(LoginActivity.this, Constants.ADMIN_ROLE);
            i = new Intent(LoginActivity.this, AdminDashBoardActivity.class);
            startActivity(i);
            finish();
        } else if (s.toLowerCase().equalsIgnoreCase(getResources().getString(R.string.hod).toLowerCase())) {
            Utility.saveRole(LoginActivity.this, Constants.HOD_ROLE);
            i = new Intent(LoginActivity.this, TeachersDashboardActivity.class);
            startActivity(i);
            finish();
        } else if (s.toLowerCase().equalsIgnoreCase(getResources().getString(R.string.faculty).toLowerCase())) {
            Utility.saveRole(LoginActivity.this, Constants.TEACHER_ROLE);
            i = new Intent(LoginActivity.this, TeachersDashboardActivity.class);
            startActivity(i);
            finish();
        }
    }
}
