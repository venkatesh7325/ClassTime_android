package org.com.classmate.ui.activities.students;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.GetClz.GetDetailsFromClzRegId.GetCollegeIDPojo;
import org.com.classmate.model.SuccessResponsePojo;
import org.com.classmate.ui.activities.admin.AdminFormActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StudentFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    CustomEditTextMedium edtStudentRollNumber;
    CustomEditTextMedium edtStudentFirstName;
    CustomEditTextMedium edtStudentLastName;
    CustomEditTextMedium edtStudentEmail;
    CustomEditTextMedium edtStudentMobile;
    CustomEditTextMedium edtStudentPassWord;
    CustomEditTextMedium edtStudentConfrmPassWord;
    Spinner spYear;
    Spinner spSemester;
    RadioGroup radioGroupStntGender;
    CustomEditTextMedium edtBranch;
    private String TAG = "Student registration";
    List<BranchList> branchListsFromPref = null;
    private String branchId = "";
    String institutionID = "";
    Button btnProceed;
    CustomEditTextMedium edtCode, edtSelectBranch;
    String clzCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_form);
        Utility.hideKeyBoard(this);// hide key board while lunching APP
        getBranchFromPref();
        initBinds();
    }


    void initBinds() {

        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(this);
        edtCode = (CustomEditTextMedium) findViewById(R.id.edt_code);

        /*imgProfile = (CircularImageView) findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(this);*/
       /* edtCvcode = (CustomTextViewMedium) findViewById(R.id.edt_cvcode);
        edtCvcode.setOnClickListener(this);
        edtStudentRollNumber = (CustomEditTextMedium) findViewById(R.id.edt_student_rollnumber);
        edtStudentFirstName = (CustomEditTextMedium) findViewById(R.id.edt_student_fname);
        edtStudentLastName = (CustomEditTextMedium) findViewById(R.id.edt_student_lname);
        edtStudentEmail = (CustomEditTextMedium) findViewById(R.id.edt_student_email);
        edtStudentMobile = (CustomEditTextMedium) findViewById(R.id.edt_student_mobile);
        edtStudentPassWord = (CustomEditTextMedium) findViewById(R.id.edt_student_password);
        edtStudentConfrmPassWord = (CustomEditTextMedium) findViewById(R.id.edt_student_cnf_password);
        edtStudentAddress = (CustomEditTextMedium) findViewById(R.id.edt_student_address);
        edtStudentCity = (CustomEditTextMedium) findViewById(R.id.edt_student_city);
        spYear = (Spinner) findViewById(R.id.sp_choose_years);
        spSemester = (Spinner) findViewById(R.id.sp_choose_semi);
        edtBranch = (CustomEditTextMedium) findViewById(R.id.edt_select_branch);
        edtBranch.setOnClickListener(this);
        edtBranch.setOnFocusChangeListener(this);
        edtBranch.setInputType(InputType.TYPE_NULL);
        radioGroupStntGender = (RadioGroup) findViewById(R.id.rg_student_gender);
        btnSaveStudentDetails = (Button) findViewById(R.id.btn_student_save);
        btnSaveStudentDetails.setOnClickListener(this);*/
    }

    void showAlertDialog() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
            builder.setMessage("Enter code");
            builder.setPositiveButton("Get Details", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String code = input.getText().toString().trim();
                    if (code.isEmpty()) {
                        ToastUtils.displayToast("Please enter code", StudentFormActivity.this);
                        builder.setCancelable(false);
                        return;
                    }
                    if (Utility.isConnectingToInternet(StudentFormActivity.this))
                        callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_TEACHER, "hod_code", code.toUpperCase());
                    else
                        ToastUtils.displayToast(Constants.no_internet_connection, StudentFormActivity.this);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void callAPIToGetCollegeDetails(String whichApi, final String postKey, final String postValues) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(postKey, postValues);

            new APIRequest(this).postStringRequest(whichApi, map, new RequestCallBack() {
                @Override
                public void onResponse(String jsonObject) {
                    Log.d(TAG, "HOD Activity--" + jsonObject);
                    Gson gson = new Gson();
                    GetCollegeIDPojo getCollegeIDPojo = gson.fromJson(jsonObject, GetCollegeIDPojo.class);
                    if (getCollegeIDPojo == null && getCollegeIDPojo.getCollegeDetails() == null)
                        return;

                    if (getCollegeIDPojo.getMessage().equals("success")) {
                        clzCode = postValues;
                        institutionID = String.valueOf(getCollegeIDPojo.getCollegeDetails().getInstitutionId());
                        branchId = String.valueOf(getCollegeIDPojo.getCollegeDetails().getBranchId());
                        showCollegeDetailsToHod(getCollegeIDPojo.getCollegeDetails().getInstitutionName() + "(" + edtCode.getText().toString().trim() + ")", String.valueOf(getCollegeIDPojo.getCollegeDetails().getBranchId()));
                    } else {
                        ToastUtils.displayToast("Please enter valid code", StudentFormActivity.this);
                    }

                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast("Please enter valid code", StudentFormActivity.this);
                    Log.d(TAG, "HOD Error message--" + message);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void showCollegeDetailsToHod(final String message, final String bID) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            builder.setTitle("Check below college details : ");
            builder.setMessage(message.toUpperCase());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    popupSubmitClzDetails(StudentFormActivity.this, bID);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                }
            });
            AlertDialog alertDialog = builder.create();
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
                alertDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
   /* private void doRegistration() {

        String stFirstName = edtStudentFirstName.getText().toString().trim();
        String stLastName = edtStudentLastName.getText().toString().trim();
        String code = edtCvcode.getText().toString().trim();
        String studentRollNumber = edtStudentRollNumber.getText().toString().trim();
        String stEmail = edtStudentEmail.getText().toString().trim();
        String stMobile = edtStudentMobile.getText().toString();

        if (TextUtils.isEmpty(code)) {
            ToastUtils.displayToast("Please enter HOD code", this);
            return;
        }
        if (TextUtils.isEmpty(studentRollNumber)) {
            ToastUtils.displayToast("Please enter your RollNumber", this);
            return;
        }
        if (studentRollNumber.length() <= 6) {
            ToastUtils.displayToast("Student Roll number minimum 6 char above", this);
            return;
        }
        if (TextUtils.isEmpty(stFirstName)) {
            ToastUtils.displayToast("Please enter first name", this);
            return;
        }
        if (stFirstName.length() < 3) {
            ToastUtils.displayToast("First name minimum 3 char above", this);
            return;
        }
        if (TextUtils.isEmpty(stLastName)) {
            ToastUtils.displayToast("Please enter last name", this);
            return;
        }
        if (!Utility.isValidEmail(stEmail)) {
            ToastUtils.displayToast("Please enter valid email", this);
            return;
        }
        if (TextUtils.isEmpty(stMobile)) {
            ToastUtils.displayToast("Please enter Mobile number", this);
            return;
        }
        if (stMobile.length() != 10) {
            ToastUtils.displayToast("Please enter 10 digit Mobile number", this);
            return;
        }
        if (returnSelection(radioGroupStntGender).isEmpty()) {
            ToastUtils.displayToast("Please select gender", this);
            return;
        }
        if (getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
            ToastUtils.displayToast("Please select year", this);
            return;
        }
        if (getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSemester.getSelectedItem().toString())) {
            ToastUtils.displayToast("Please select semester", this);
            return;
        }

        String password = edtStudentPassWord.getText().toString().trim(), cnfpassword = edtStudentConfrmPassWord.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.displayToast("Please enter Password", this);
            return;
        }
        if (password.length() < 6) {
            ToastUtils.displayToast("Password minimum 6 char above", this);
            return;
        }
        if (TextUtils.isEmpty(cnfpassword)) {
            ToastUtils.displayToast("Please enter Confirm Password", this);
            return;
        }

        if (!password.equals(cnfpassword)) {
            ToastUtils.displayToast("password did not match", this);
            return;
        }
        if (stCity.length() < 3) {
            ToastUtils.displayToast("Please Enter Valid City ", this);
            return;
        }

        if (stAdd.length() < 3) {
            ToastUtils.displayToast("Please Enter Valid Address ", this);
            return;
        }

*//*{ "name": "Christ Student","roll_number":"9999999999", "email": "christ_student@gmail.com",
"gender_id":"1", "password": "123456", "branch_id": "1", "address": "address",
 "city": "city", "code": "CRITHD5", "role_id": "4", "batch_id": "1",
 "qualification": "BE", "mobile": "7878787877", "institution_id":"1" }*//*

        final HashMap<String, String> jsonObject = new HashMap<String, String>();
        try {
            jsonObject.put("name", stFirstName);
            jsonObject.put("roll_number", studentRollNumber);
            jsonObject.put("code", code);
            jsonObject.put("role_id", Constants.STUDENT_ROLE);
            jsonObject.put("email", stEmail);
            jsonObject.put("password", password);
            jsonObject.put("address", stAdd);
            jsonObject.put("city", stCity);
            jsonObject.put("branch_id", branchId);
            jsonObject.put("institution_id", institutionID);
            jsonObject.put("qualification", "");
            jsonObject.put("mobile", stMobile);
            jsonObject.put("gender_id", returnSelection(radioGroupStntGender));
            //"current_year": "2017", "year": "1", "semester": "1"
            Calendar cal = Calendar.getInstance();
            jsonObject.put("current_year", String.valueOf(cal.get(Calendar.YEAR)));
            jsonObject.put("year", Constants.getYearId(spYear.getSelectedItem().toString()));
            jsonObject.put("semester", Constants.getYearId(spSemester.getSelectedItem().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Hash Map aprams--" + jsonObject);
        *//*Intent intent = new Intent(this, StudentDayActivity.class);
        startActivity(intent);*//*
        new APIRequest(this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
            @Override
            public void onResponse(String jsonObject) {
                ToastUtils.displayToast(jsonObject, StudentFormActivity.this);
                Log.d(TAG, "Student Activity--" + jsonObject);
                Gson gson = new Gson();
                SuccessResponsePojo successResponsePojo = gson.fromJson(jsonObject, SuccessResponsePojo.class);
                if (successResponsePojo.getStatus().equals("1")) {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                    Utility.saveUserID(StudentFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                    Bundle b = new Bundle();
                    b.putString("mode_of_login", "Student");
                    Intent i = new Intent(StudentFormActivity.this, LoginActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, StudentFormActivity.this);
                Log.d(TAG, "Student Error message--" + message + "--jon respons--" + jsonObject);

            }
        });
    }*/

    private String returnSelection(RadioGroup radioGroup) {
        int id = radioGroup.getCheckedRadioButtonId();
        View radioButton = radioGroup.findViewById(id);
        int radioId = radioGroup.indexOfChild(radioButton);
        RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
        String selection = (String) btn.getText();
        Log.d(TAG, "Selected mode--" + selection);
        String genderId;
        if (selection.equals("Male")) {
            genderId = "1";
        } else
            genderId = "2";
        return genderId;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_proceed:
                String code = edtCode.getText().toString().trim();
                if (code.isEmpty()) {
                    ToastUtils.displayToast("Please enter code", StudentFormActivity.this);
                    return;
                }
                if (Utility.isConnectingToInternet(StudentFormActivity.this))
                    callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_TEACHER, "hod_code", code.toUpperCase());
                else
                    ToastUtils.displayToast(Constants.no_internet_connection, StudentFormActivity.this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            return;
        }
        loadBranchAndSelect();

    }

    private void loadBranchAndSelect() {
        /*InputMethodManager imm = (InputMethodManager) StudentFormActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtBranch.getWindowToken(), 0);*/
        List<String> branchNameList = new ArrayList<>();
        for (int i = 0; i < branchListsFromPref.size(); i++) {
            branchNameList.add(branchListsFromPref.get(i).getBranchName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(StudentFormActivity.this, android.R.layout.simple_list_item_1, branchNameList);
        AlertDialog.Builder builder = new AlertDialog.Builder(StudentFormActivity.this);
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


    private void popupSubmitClzDetails(final Context context, final String bId) {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_student_details);
            dialog.setTitle("Fill your Details");
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            }
            dialog.show();

            edtStudentRollNumber = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_rollnumber);
            edtStudentFirstName = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_fname);
            edtStudentLastName = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_lname);
            edtStudentEmail = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_email);
            edtStudentMobile = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_mobile);
            edtStudentPassWord = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_password);
            edtStudentConfrmPassWord = (CustomEditTextMedium) dialog.findViewById(R.id.edt_student_cnf_password);
            edtSelectBranch = (CustomEditTextMedium) dialog.findViewById(R.id.edt_select_branch);
            radioGroupStntGender = (RadioGroup) dialog.findViewById(R.id.rg_student_gender);
            spYear = (Spinner) dialog.findViewById(R.id.sp_choose_years);
            spSemester = (Spinner) dialog.findViewById(R.id.sp_choose_semi);

            edtSelectBranch.setOnClickListener(this);
            edtSelectBranch.setOnFocusChangeListener(this);
            edtSelectBranch.setInputType(InputType.TYPE_NULL);
            CustomTextViewMedium ctvSave = (CustomTextViewMedium) dialog.findViewById(R.id.tv_save);

            if (branchListsFromPref == null)
                return;
            for (int i = 0; i < branchListsFromPref.size(); i++) {
                if (branchId.equals(String.valueOf(branchListsFromPref.get(i).getBranchId()))) {
                    edtSelectBranch.setText(branchListsFromPref.get(i).getBranchName());
                }
            }
            ctvSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String stFirstName = edtStudentFirstName.getText().toString().trim();
                    String stLastName = edtStudentLastName.getText().toString().trim();
                    String studentRollNumber = edtStudentRollNumber.getText().toString().trim();
                    String stEmail = edtStudentEmail.getText().toString().trim();
                    String stMobile = edtStudentMobile.getText().toString();


                    if (TextUtils.isEmpty(studentRollNumber)) {
                        ToastUtils.displayToast("Please enter your RollNumber", context);
                        return;
                    }
                    if (studentRollNumber.length() <= 6) {
                        ToastUtils.displayToast("Student Roll number minimum 6 char above", StudentFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(stFirstName)) {
                        ToastUtils.displayToast("Please enter first name", StudentFormActivity.this);
                        return;
                    }
                    if (stFirstName.length() < 3) {
                        ToastUtils.displayToast("First name minimum 3 char above", StudentFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(stLastName)) {
                        ToastUtils.displayToast("Please enter last name", StudentFormActivity.this);
                        return;
                    }
                    if (!Utility.isValidEmail(stEmail)) {
                        ToastUtils.displayToast("Please enter valid email", StudentFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(stMobile)) {
                        ToastUtils.displayToast("Please enter Mobile number", StudentFormActivity.this);
                        return;
                    }
                    if (stMobile.length() != 10) {
                        ToastUtils.displayToast("Please enter 10 digit Mobile number", StudentFormActivity.this);
                        return;
                    }
                    if (returnSelection(radioGroupStntGender).isEmpty()) {
                        ToastUtils.displayToast("Please select gender", StudentFormActivity.this);
                        return;
                    }
                    if (getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select year", StudentFormActivity.this);
                        return;
                    }
                    if (getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSemester.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select semester", StudentFormActivity.this);
                        return;
                    }

                    String password = edtStudentPassWord.getText().toString().trim(), cnfpassword = edtStudentConfrmPassWord.getText().toString().trim();
                    if (TextUtils.isEmpty(password)) {
                        ToastUtils.displayToast("Please enter Password", StudentFormActivity.this);
                        return;
                    }
                    if (password.length() < 6) {
                        ToastUtils.displayToast("Password minimum 6 char above", StudentFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(cnfpassword)) {
                        ToastUtils.displayToast("Please enter Confirm Password", StudentFormActivity.this);
                        return;
                    }

                    if (!password.equals(cnfpassword)) {
                        ToastUtils.displayToast("password did not match", StudentFormActivity.this);
                        return;
                    }
                    final HashMap<String, String> jsonObject = new HashMap<String, String>();
                    try {
                        jsonObject.put("name", stFirstName);
                        jsonObject.put("roll_number", studentRollNumber);
                        jsonObject.put("code", clzCode);
                        jsonObject.put("role_id", Constants.STUDENT_ROLE);
                        jsonObject.put("email", stEmail);
                        jsonObject.put("password", password);
                        jsonObject.put("address", "NA");
                        jsonObject.put("city", "NA");
                        jsonObject.put("branch_id", branchId);
                        jsonObject.put("institution_id", institutionID);
                        jsonObject.put("qualification", "");
                        jsonObject.put("mobile", stMobile);
                        jsonObject.put("gender_id", returnSelection(radioGroupStntGender));
                        //"current_year": "2017", "year": "1", "semester": "1"
                        Calendar cal = Calendar.getInstance();
                        jsonObject.put("current_year", String.valueOf(cal.get(Calendar.YEAR)));
                        jsonObject.put("year", Constants.getYearId(spYear.getSelectedItem().toString()));
                        jsonObject.put("semester", Constants.getYearId(spSemester.getSelectedItem().toString()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Hash Map aprams--" + jsonObject);

                    new APIRequest(StudentFormActivity.this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
                        @Override
                        public void onResponse(String jsonObject) {
                            ToastUtils.displayToast(jsonObject, StudentFormActivity.this);
                            Log.d(TAG, "Student Activity--" + jsonObject);
                            Gson gson = new Gson();
                            SuccessResponsePojo successResponsePojo = gson.fromJson(jsonObject, SuccessResponsePojo.class);
                            if (successResponsePojo.getStatus().equals("1")) {
                                ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                                Utility.saveRole(StudentFormActivity.this, Constants.STUDENT_ROLE);
                                Utility.saveLoginID(StudentFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                Bundle b = new Bundle();
                                b.putString("mode_of_login", "Student");
                                Intent i = new Intent(StudentFormActivity.this, StudentDashBoardActivity.class);
                                //  i.putExtras(b);
                                startActivity(i);
                                finish();
                            } else {
                                ToastUtils.displayToast(successResponsePojo.getError(), StudentFormActivity.this);
                            }
                        }

                        @Override
                        public void onFailed(String message) {
                            ToastUtils.displayToast(Constants.something_went_wrong, StudentFormActivity.this);
                            Log.d(TAG, "Student Error message--" + message + "--jon respons--" + jsonObject);

                        }
                    });

                 /*   new APIRequest(StudentFormActivity.this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "HOD Activity--" + response);
                            Gson gson = new Gson();
                            SuccessResponsePojo successResponsePojo = gson.fromJson(response, SuccessResponsePojo.class);
                            if (successResponsePojo.getStatus().equals("1")) {
                                ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                                Utility.saveUserID(StudentFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                                Bundle b = new Bundle();
                                b.putString("mode_of_login", selectionMode);
                                Intent i = new Intent(StudentFormActivity.this, TeachersDashboardActivity.class);
                                i.putExtras(b);
                                startActivity(i);
                                finish();
                            } else {
                                ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                            }
                        }

                        @Override
                        public void onFailed(String message) {
                            ToastUtils.displayToast(Constants.something_went_wrong, StudentFormActivity.this);
                            Log.d(TAG, "HOD Error message--" + message + "--jon respons--" + jsonObject);

                        }
                    });

                    if (dialog.isShowing())
                        dialog.dismiss();
*/
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
