package org.com.classmate.ui.activities.teacher;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.adapter.StudentListAdapter;
import org.com.classmate.adapter.TeacherAttendanceAdapter;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.LoginResponseModel.LoginResponsePojo;
import org.com.classmate.model.students.StudentsList.StudentList;
import org.com.classmate.model.teacher.teacher_attendance_module.AttendanceReport;
import org.com.classmate.model.teacher.teacher_attendance_module.AttendnceReport;
import org.com.classmate.model.teacher.teacher_attendance_module.SubmitAttendancePojo;
import org.com.classmate.model.teacher.teacher_attendance_module.TeacherAttendanceModel;
import org.com.classmate.ui.activities.common.LoginActivity;
import org.com.classmate.ui.activities.students.StudentFormActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TeacherAttendanceActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "TeacherAttendanceAct";
    RecyclerView rcvStudentsList;
    Button btnSelectedRg;
    TeacherAttendanceAdapter teacherAttendanceAdapter;
    List<AttendnceReport> teacherAttendanceModelsList;
    @Bind(R.id.edt_attendanceDate)
    EditText edtAttendanceDate;
    List<BranchList> branchListsFromPref = null;
    private String branchId = "";
    CustomEditTextMedium edtBranch;
    List<StudentList> getStudentsListPojo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_attendance);
        ButterKnife.bind(this);
        getBranchFromPref();
        init();
    }

    void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Attendance");
        edtBranch = (CustomEditTextMedium) findViewById(R.id.edt_select_branch);
        edtBranch.setOnClickListener(this);
        edtBranch.setOnFocusChangeListener(this);
        edtBranch.setInputType(InputType.TYPE_NULL);
        rcvStudentsList = (RecyclerView) findViewById(R.id.rcv_students_attendance);
        btnSelectedRg = (Button) findViewById(R.id.btn_submit_attendance);
        btnSelectedRg.setOnClickListener(this);

        edtAttendanceDate.setOnClickListener(this);
        edtAttendanceDate.setInputType(InputType.TYPE_NULL);
        edtAttendanceDate.setCursorVisible(false);
        getParecDataFromBundle();
    }

    void getParecDataFromBundle() {
        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                getStudentsListPojo = getIntent().getExtras().getParcelableArrayList("student_list");
                if (getStudentsListPojo != null && getStudentsListPojo.size() > 0)
                    setAdapter(getStudentsListPojo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void setAdapter(List<StudentList> getStudentsListPojo) {
        if (getStudentsListPojo != null && !getStudentsListPojo.isEmpty()) {
            teacherAttendanceAdapter = new TeacherAttendanceAdapter(this, getStudentsListPojo);
            rcvStudentsList.setLayoutManager(new LinearLayoutManager(this));
            rcvStudentsList.setAdapter(teacherAttendanceAdapter);
        } else {
            ToastUtils.displayToast("No students for this class", this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_attendance:
                submitAttendanceMethod();
                break;
            case R.id.edt_attendanceDate:
                Utility.showDatePickerDialog(this, edtAttendanceDate, "Attendance Date");
                break;
            case R.id.edt_select_branch:
                loadBranchAndSelect();
                break;
        }
    }

    void submitAttendanceMethod() {
        if (!teacherAttendanceAdapter.getSlectedAttendanceList().isEmpty()) {
            if (getStudentsListPojo.size() == teacherAttendanceAdapter.getSlectedAttendanceList().size()) {
                Log.d(TAG, "GET in Activity selected list--" + teacherAttendanceAdapter.getSlectedAttendanceList().size());
                // Log.d(TAG, "GET in 1st selected list--" + teacherAttendanceAdapter.getSlectedAttendanceList().get(0).getAttendanceStatus());
                List<AttendanceReport> list = new ArrayList<>();
                for (int i = 0; i < getStudentsListPojo.size(); i++) {
                    list.add(teacherAttendanceAdapter.getSlectedAttendanceList().get(getStudentsListPojo.get(i).getRollNumber()));
                }
                if (list.size() > 0)
                    callApiToSubmitAttendance(list);
                Log.d(TAG, "2 GET in Activity selected list--" + list.get(0).getAttendanceStatus());
            } else {
                ToastUtils.displayToast("Please give all students attendance...", this);
            }
        } else {
            ToastUtils.displayToast("Please select student attendance", this);
        }
    }

    void callApiToSubmitAttendance(List<AttendanceReport> list) {
        SubmitAttendancePojo submitAttendancePojo = new SubmitAttendancePojo();
        submitAttendancePojo.setBranchId("10");
        submitAttendancePojo.setYear("1");
        submitAttendancePojo.setSemester("1");
        submitAttendancePojo.setTeacherId(String.valueOf(Utility.getUserID(TeacherAttendanceActivity.this)));
        submitAttendancePojo.setAttendanceDate(edtAttendanceDate.getText().toString());
        submitAttendancePojo.setAttendanceReport(list);
        Gson gson = new Gson();
        Type type = new TypeToken<SubmitAttendancePojo>() {
        }.getType();
        String attendanceResponse = gson.toJson(submitAttendancePojo, type);
        Log.d(TAG, "Attendance Response--" + attendanceResponse);
        //ToastUtils.displayToast("Success", this);
        //ToastUtils.displayToast("Success", this);
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
           // hashMap.put("id", String.valueOf(Utility.getUserID(TeacherAttendanceActivity.this)));
            hashMap.put("", attendanceResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "TeacherAttendanceActivity Activity HASHAMP--" + hashMap);
        new APIRequest(this).postStringRequest(ApiConstants.SUBMIT_ATTENDANCE, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Activity--" + response);
                Gson gson = new Gson();
                LoginResponsePojo successResponsePojo = gson.fromJson(response, LoginResponsePojo.class);
               /* if (successResponsePojo.getMessage().equalsIgnoreCase("success")) {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                    Utility.saveUserID(LoginActivity.this, successResponsePojo.getId()); // saving user ID into pref
                    moveToDashboard(selectionMode);

                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), LoginActivity.this);
                }*/

            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, TeacherAttendanceActivity.this);
                Log.d(TAG, "TeacherAttendanceActivity Error message--" + message + "--jon respons--" + hashMap);

            }
        });

    }

    private void getBranchFromPref() {
        try {
            String unvList = Utility.getBranchListList(this);
            Gson gson = new Gson();
            Type type = new TypeToken<List<BranchList>>() {
            }.getType();
            branchListsFromPref = gson.fromJson(unvList, type);
            Log.d(TAG, "branch-list_oncreate--" + branchListsFromPref.size() + "--fjk--" + branchListsFromPref.get(0).getBranchName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBranchAndSelect() {
        /*InputMethodManager imm = (InputMethodManager) HodFormActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtBranch.getWindowToken(), 0);*/
        List<String> branchNameList = new ArrayList<>();
        for (int i = 0; i < branchListsFromPref.size(); i++) {
            branchNameList.add(branchListsFromPref.get(i).getBranchName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeacherAttendanceActivity.this, android.R.layout.simple_list_item_1, branchNameList);
        AlertDialog.Builder builder = new AlertDialog.Builder(TeacherAttendanceActivity.this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                edtBranch.setText(branchListsFromPref.get(item).getBranchName());
                edtBranch.setTag(branchListsFromPref.get(item).getBranchId());
                Log.d(TAG, "Branch Name " + branchListsFromPref.get(item).getBranchName());
                Log.d(TAG, "Branch Id " + branchListsFromPref.get(item).getBranchId());
                branchId = String.valueOf(branchListsFromPref.get(item).getBranchId());
                dialog.cancel();
            }
        });
        final AlertDialog dialog = builder.create();
        if (branchListsFromPref.size() != 0)
            dialog.show();

    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            return;
        }
        loadBranchAndSelect();
    }
}
