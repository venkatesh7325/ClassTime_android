package org.com.classmate.ui.activities.teacher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.APIS.StudentListResponse;
import org.com.classmate.R;
import org.com.classmate.adapter.TodaySubjectsAdapter;
import org.com.classmate.model.GetClz.GetCollegeResponsePojo;
import org.com.classmate.model.hod.TeacherData.GetTeacherDetailsModel;
import org.com.classmate.model.students.StudentsList.GetStudentsListPojo;
import org.com.classmate.ui.activities.common.LoginActivity;
import org.com.classmate.ui.activities.hod.TaskListActivity;
import org.com.classmate.ui.activities.students.StudentsListActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TeachersDashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "TeachsDashbActivity";
    CustomTextViewMedium tvCode;
    CustomTextViewMedium tvName;
    CustomTextViewMedium tvDesignation;

    /* @Bind(R.id.TaskAssign)
         LinearLayout taskAssign;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);
        ButterKnife.bind(this);
        initToolBar();
        tvCode = (CustomTextViewMedium) findViewById(R.id.txtDCode);
        tvName = (CustomTextViewMedium) findViewById(R.id.txtAdminName);
        tvDesignation = (CustomTextViewMedium) findViewById(R.id.txtDesignation);

        if (Utility.isConnectingToInternet(this))
            callApiToGetUserDetails(this);
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);

        findViewById(R.id.ll_Tim_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.isConnectingToInternet(TeachersDashboardActivity.this)) {
                    ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);
                    return;
                }
                startActivity(new Intent(TeachersDashboardActivity.this, TeachersTimeTableActivity.class));
            }
        });
        findViewById(R.id.ll_classes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.isConnectingToInternet(TeachersDashboardActivity.this)) {
                    ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);
                    return;
                }
                startActivity(new Intent(TeachersDashboardActivity.this, LessonActivity.class));
                // Toast.makeText(TeachersDashboardActivity.this, "Coming soon..", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.ll_task_creation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utility.isConnectingToInternet(TeachersDashboardActivity.this)) {
                    ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);
                    return;
                }
                startActivity(new Intent(TeachersDashboardActivity.this, TaskListActivity.class));
            }
        });

        findViewById(R.id.ll_attendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!Utility.isConnectingToInternet(TeachersDashboardActivity.this)) {
                    ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);
                    return;
                }
                popupToGetStudentsList(TeachersDashboardActivity.this);*/
                if (!Utility.isConnectingToInternet(TeachersDashboardActivity.this)) {
                    ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);
                    return;
                }
                startActivity(new Intent(TeachersDashboardActivity.this, TeacherAttendanceActivity.class));
            }
        });

    }

    void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("HOD Dashboard");
    }

    @Override
    public void onClick(View v) {
        Intent i = null;
        /*switch (v.getId()){
            case R.id.TaskAssign:
                i = new Intent(this,TeachersTaskAssignActivity.class_stu);
                startActivity(i);
                break;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Constants.popupToLogOut(TeachersDashboardActivity.this);
        }
        return super.onOptionsItemSelected(item);
    }

    void callApiToGetUserDetails(final Context context) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            hashMap.put("id", String.valueOf(Utility.getLoginID(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Teach User--" + hashMap);
        new APIRequest(context).postStringRequest(ApiConstants.GET_USER_DETAILS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Teach Activity--" + response);
                Gson gson = new Gson();
                GetTeacherDetailsModel successResponsePojo = gson.fromJson(response, GetTeacherDetailsModel.class);
                if (successResponsePojo.getMessage().equalsIgnoreCase("success")) {
                    if (successResponsePojo.getUserDetails().getCode() != null)
                        tvCode.setText(successResponsePojo.getUserDetails().getCode());
                    tvName.setText(successResponsePojo.getUserDetails().getName());
                    tvDesignation.setText("B-Tech");
                    Utility.saveUserID(TeachersDashboardActivity.this, successResponsePojo.getUserDetails().getId()); // saving user ID into pref
                    Utility.saveInstitutionID(TeachersDashboardActivity.this, String.valueOf(successResponsePojo.getUserDetails().getInstitutionId()));
                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), context);
                }

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, context);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }

    private void popupToGetStudentsList(Context context) {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.Animation));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.discount_popup);
            dialog.setTitle("Select Year and Semester");
            if (dialog.getWindow() != null)
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            ImageView imageClose = (ImageView) dialog.findViewById(R.id.close);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });

            final Spinner spYear = (Spinner) dialog.findViewById(R.id.sp_choose_years);
            final Spinner spSem = (Spinner) dialog.findViewById(R.id.sp_choose_semi);

            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select year", TeachersDashboardActivity.this);
                        return;
                    }

                    if (getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSem.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select semester", TeachersDashboardActivity.this);
                        return;
                    }

                    if (Utility.isConnectingToInternet(TeachersDashboardActivity.this))
                        APIRequest.callApiToGetStudentList(TeachersDashboardActivity.this, Constants.getYearId(spYear.getSelectedItem().toString()), Constants.getYearId(spSem.getSelectedItem().toString()), "10", Utility.getInstitutionId(TeachersDashboardActivity.this), new StudentListResponse() {
                            @Override
                            public void stdList(GetStudentsListPojo getStudentsListPojo) {
                                Logger.logD(TAG, "List-size--" + getStudentsListPojo.getStudentList().size());
                                Intent i = new Intent(TeachersDashboardActivity.this, TeacherAttendanceActivity.class);
                                i.putParcelableArrayListExtra("student_list", (ArrayList<? extends Parcelable>) getStudentsListPojo.getStudentList());
                                TeachersDashboardActivity.this.startActivity(i);
                            }
                        });
                    else
                        ToastUtils.displayToast(Constants.no_internet_connection, TeachersDashboardActivity.this);

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
