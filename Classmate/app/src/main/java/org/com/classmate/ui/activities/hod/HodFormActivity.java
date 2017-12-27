package org.com.classmate.ui.activities.hod;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.GetClz.GetDetailsFromClzRegId.GetCollegeIDPojo;
import org.com.classmate.model.SuccessResponsePojo;
import org.com.classmate.ui.activities.admin.AdminFormActivity;
import org.com.classmate.ui.activities.common.LoginActivity;
import org.com.classmate.ui.activities.teacher.TeachersDashboardActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HodFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "HodFormActivity";

    String selectionMode = "";
    String regRollId = "";
    String institutionID = "";
    String branchId = "";
    List<BranchList> branchListsFromPref = null;
    //  CustomTextViewMedium tvEnterVerificationCode;
    Button btnProceed;
    CustomEditTextMedium edtCode, edtSelectBranch;
    String clzCode = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hod_form);
        Utility.hideKeyBoard(this);// hide key board while lunching APP
        getBranchFromPref();
        getDateFromPreviousActivity();
        bindViews();
    }

    private void getDateFromPreviousActivity() {
        try {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                selectionMode = b.getString("mode_of_login");
                Log.d(TAG, "Mode--" + selectionMode);
                if (selectionMode.equals("HOD"))
                    regRollId = Constants.HOD_ROLE;
                else
                    regRollId = Constants.TEACHER_ROLE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void bindViews() {
        //  tvEnterVerificationCode = (CustomTextViewMedium) findViewById(R.id.tv_clz_verification_code);
        /*if (!"HOD".equals(selectionMode)) {
            tvEnterVerificationCode.setHint("Enter HOD verification code");
        }*/
        btnProceed = (Button) findViewById(R.id.btn_proceed);
        btnProceed.setOnClickListener(this);
        edtCode = (CustomEditTextMedium) findViewById(R.id.edt_code);
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
        /*InputMethodManager imm = (InputMethodManager) HodFormActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edtBranch.getWindowToken(), 0);*/
        List<String> branchNameList = new ArrayList<>();
        for (int i = 0; i < branchListsFromPref.size(); i++) {
            branchNameList.add(branchListsFromPref.get(i).getBranchName());
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(HodFormActivity.this, android.R.layout.simple_list_item_1, branchNameList);
        AlertDialog.Builder builder = new AlertDialog.Builder(HodFormActivity.this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                //edtBranch.setText(branchListsFromPref.get(item).getBranchName());
                //edtBranch.setTag(branchListsFromPref.get(item).getBranchId());
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

   /* private void doRegistration() {
        try {
            Log.d(TAG, "Reg 1");
            String firstName = hodFirstName.getText().toString().trim();
            if (institutionID.isEmpty()) {
                if (selectionMode.equals("HOD"))
                    ToastUtils.displayToast("Please enter ADMIN verification code", this);
                else
                    ToastUtils.displayToast("Please enter HOD verification code", this);
                return;
            }
            if (TextUtils.isEmpty(hodFirstName.getText().toString().trim())) {
                ToastUtils.displayToast("Please enter first name", this);
                return;
            }
            if (firstName.length() < 3) {
                ToastUtils.displayToast("First name minimum 3 char above", this);
                return;
            }
            if (TextUtils.isEmpty(hodLastName.getText().toString().trim())) {
                ToastUtils.displayToast("Please enter last name", this);
                return;
            }

            if (TextUtils.isEmpty(edtQualification.getText().toString().trim())) {
                ToastUtils.displayToast("Please enter your qualification", this);
                return;
            }
            if (edtQualification.getText().toString().trim().length() < 1) {
                ToastUtils.displayToast("Qualification minimum 1 char above", this);
                return;
            }
            if (returnSelection(hodRgGender).isEmpty()) {
                ToastUtils.displayToast("Please select gender", this);
                return;
            }
            if (!Utility.isValidEmail(hodEmail.getText().toString())) {
                ToastUtils.displayToast("Please enter valid email", this);
                return;
            }

            String password = hodPassword.getText().toString().trim(), cnfpassword = hodCnfmPassword.getText().toString().trim();
            if (TextUtils.isEmpty(hodPassword.getText().toString().trim())) {
                ToastUtils.displayToast("Please enter Password", this);
                return;
            }
            if (password.length() <= 5) {
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
            if (TextUtils.isEmpty(hodMobile.getText().toString().trim())) {
                ToastUtils.displayToast("Please enter Mobile number", this);
                return;
            }
            if (hodMobile.getText().toString().trim().length() != 10) {
                ToastUtils.displayToast("Please enter 10 digit Mobile number", this);
                return;
            }
            if (hodAddress.getText().toString().length() < 3) {
                ToastUtils.displayToast("Please Enter address ", this);
                return;
            }

            if (hodCity.getText().toString().length() < 3) {
                ToastUtils.displayToast("Please Enter Valid City ", this);
                return;
            }
            Log.d(TAG, "Reg 01 --" + hodFirstName.getText().toString().trim());
            Log.d(TAG, "Reg 2");
            final HashMap<String, String> jsonObject = new HashMap<>();
            try {
                jsonObject.put("name", hodFirstName.getText().toString().trim());
                jsonObject.put("code", collegeVerficationCode.getText().toString().trim());
                jsonObject.put("role_id", regRollId);
                jsonObject.put("email", hodEmail.getText().toString().trim());
                jsonObject.put("password", hodPassword.getText().toString().trim());
                jsonObject.put("address", hodAddress.getText().toString().trim());
                jsonObject.put("branch_id", branchId);
                jsonObject.put("institution_id", institutionID);
                jsonObject.put("city", hodCity.getText().toString().trim());
                jsonObject.put("qualification", edtQualification.getText().toString().trim());
                jsonObject.put("mobile", hodMobile.getText().toString().trim());
                jsonObject.put("gender_id", returnSelection(hodRgGender));

            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "After hash map");
            Log.d(TAG, "Hash Map aprams--" + jsonObject);
            new APIRequest(this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "HOD Activity--" + response);
                    Gson gson = new Gson();
                    SuccessResponsePojo successResponsePojo = gson.fromJson(response, SuccessResponsePojo.class);
                    if (successResponsePojo.getStatus().equals("1")) {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), HodFormActivity.this);
                        Utility.saveUserID(HodFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                        Bundle b = new Bundle();
                        b.putString("mode_of_login", selectionMode);
                        Intent i = new Intent(HodFormActivity.this, LoginActivity.class);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    } else {
                        ToastUtils.displayToast(successResponsePojo.getMessage(), HodFormActivity.this);
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, HodFormActivity.this);
                    Log.d(TAG, "HOD Error message--" + message + "--jon respons--" + jsonObject);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*{ "name": "Christ HOD", "email": "christHod@gmail.com",
            "gender_id":"1", "password": "123456", "branch_id": "1", "address": "address", "city": "city",
            "code": "CRITAD1", "role_id": "2", "institution_id": "1", "qualification": "BE", "mobile": "9898989546" }*/
    private String returnSelection(RadioGroup radioGroup) {
        String genderId = "";
        try {
            int id = radioGroup.getCheckedRadioButtonId();
            View radioButton = radioGroup.findViewById(id);
            int radioId = radioGroup.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) radioGroup.getChildAt(radioId);
            String selection = (String) btn.getText();
            Log.d(TAG, "Selected mode--" + selection);

            if (selection.equals("Male")) {
                genderId = "1";
            } else
                genderId = "2";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return genderId;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_proceed:
                String code = edtCode.getText().toString().trim();
                if (code.isEmpty()) {
                    ToastUtils.displayToast("Please enter code", HodFormActivity.this);
                    return;
                }
                if (regRollId.equals(Constants.HOD_ROLE))
                    callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_HOD, "college_code", code.toUpperCase());
                else
                    callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_TEACHER, "hod_code", code.toUpperCase());
                break;
           /* case R.id.edt_select_branch:
                if (selectionMode.equals("HOD"))
                    loadBranchAndSelect();
                break;*/
            default:
                break;

        }

    }

    /*void showAlertDialog() {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            builder.setView(input);
            builder.setMessage("Enter code");
            builder.setPositiveButton("Get Details", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String code = input.getText().toString().trim();
                    if (code.isEmpty()) {
                        ToastUtils.displayToast("Please enter code", HodFormActivity.this);
                        builder.setCancelable(false);
                        return;
                    }
                    collegeVerficationCode.setText(code.toUpperCase());
                    if (regRollId.equals(Constants.HOD_ROLE))
                        callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_HOD, "college_code", code.toUpperCase());
                    else
                        callAPIToGetCollegeDetails(ApiConstants.GET_COLLEGE_DETAILS_TEACHER, "hod_code", code.toUpperCase());
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
    }*/

    void showCollegeDetailsToHod(final String message, final String bID) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            builder.setTitle("Check below college details : ");
            builder.setMessage(message.toUpperCase());
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    popupSubmitClzDetails(HodFormActivity.this, bID);
                    // ToastUtils.displayToast(message, HodFormActivity.this);
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

    void callAPIToGetCollegeDetails(String whichApi, final String postKey, String postValues) {
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
                        clzCode = edtCode.getText().toString().trim();
                        institutionID = String.valueOf(getCollegeIDPojo.getCollegeDetails().getInstitutionId());
                        Log.d(TAG, "Branch ID--" + getCollegeIDPojo.getCollegeDetails().getBranchId());
                        showCollegeDetailsToHod(getCollegeIDPojo.getCollegeDetails().getInstitutionName() + "(" + edtCode.getText().toString().trim() + ")", String.valueOf(getCollegeIDPojo.getCollegeDetails().getBranchId()));

                    } else {
                        ToastUtils.displayToast("Please enter valid code", HodFormActivity.this);
                    }

                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast("Please enter valid code", HodFormActivity.this);
                    Log.d(TAG, "HOD Error message--" + message);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (!b) {
            return;
        }
        if (selectionMode.equals("HOD"))
            loadBranchAndSelect();

    }

    private void popupSubmitClzDetails(final Context context, final String bId) {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popupp_hod_details);
            dialog.setTitle("Fill your Details");
            if (dialog.getWindow() != null) {
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
            }
            dialog.show();
            final CustomEditTextMedium hodFirstName = (CustomEditTextMedium) dialog.findViewById(R.id.edt_hod_fname);
            final CustomEditTextMedium hodLastName = (CustomEditTextMedium) dialog.findViewById(R.id.edt_hod_lname);
            final CustomEditTextMedium hodEmail = (CustomEditTextMedium) dialog.findViewById(R.id.edt_email);
            final CustomEditTextMedium hodMobile = (CustomEditTextMedium) dialog.findViewById(R.id.edt_mobile);
            final CustomEditTextMedium hodPassword = (CustomEditTextMedium) dialog.findViewById(R.id.edt_password);
            final CustomEditTextMedium hodCnfmPassword = (CustomEditTextMedium) dialog.findViewById(R.id.edt_cnf_password);
            final RadioGroup hodRgGender = (RadioGroup) dialog.findViewById(R.id.rg_gender);
            final CustomEditTextMedium edtQualification = (CustomEditTextMedium) dialog.findViewById(R.id.edt_hod_qualification);
            edtSelectBranch = (CustomEditTextMedium) dialog.findViewById(R.id.edt_select_branch);
            edtSelectBranch.setOnClickListener(this);
            edtSelectBranch.setOnFocusChangeListener(this);
            edtSelectBranch.setInputType(InputType.TYPE_NULL);
            CustomTextViewMedium ctvSave = (CustomTextViewMedium) dialog.findViewById(R.id.tv_save);
            if (selectionMode.equals("Teacher"))
                branchId = String.valueOf(bId);
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
                    String firstName = hodFirstName.getText().toString().trim();
                    if (TextUtils.isEmpty(hodFirstName.getText().toString().trim())) {
                        ToastUtils.displayToast("Please enter first name", HodFormActivity.this);
                        return;
                    }
                    if (firstName.length() < 3) {
                        ToastUtils.displayToast("First name minimum 3 char above", HodFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(hodLastName.getText().toString().trim())) {
                        ToastUtils.displayToast("Please enter last name", HodFormActivity.this);
                        return;
                    }

                    if (TextUtils.isEmpty(edtQualification.getText().toString().trim())) {
                        ToastUtils.displayToast("Please enter your qualification", HodFormActivity.this);
                        return;
                    }
                    if (edtQualification.getText().toString().trim().length() < 1) {
                        ToastUtils.displayToast("Qualification minimum 1 char above", HodFormActivity.this);
                        return;
                    }
                    if (returnSelection(hodRgGender).isEmpty()) {
                        ToastUtils.displayToast("Please select gender", HodFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(hodMobile.getText().toString().trim())) {
                        ToastUtils.displayToast("Please enter Mobile number", HodFormActivity.this);
                        return;
                    }
                    if (hodMobile.getText().toString().trim().length() != 10) {
                        ToastUtils.displayToast("Please enter 10 digit Mobile number", HodFormActivity.this);
                        return;
                    }

                    if (!Utility.isValidEmail(hodEmail.getText().toString())) {
                        ToastUtils.displayToast("Please enter valid email", HodFormActivity.this);
                        return;
                    }

                    String password = hodPassword.getText().toString().trim(), cnfpassword = hodCnfmPassword.getText().toString().trim();
                    if (TextUtils.isEmpty(hodPassword.getText().toString().trim())) {
                        ToastUtils.displayToast("Please enter Password", HodFormActivity.this);
                        return;
                    }
                    if (password.length() <= 5) {
                        ToastUtils.displayToast("Password minimum 6 char above", HodFormActivity.this);
                        return;
                    }
                    if (TextUtils.isEmpty(cnfpassword)) {
                        ToastUtils.displayToast("Please enter Confirm Password", HodFormActivity.this);
                        return;
                    }
                    if (!password.equals(cnfpassword)) {
                        ToastUtils.displayToast("password did not match", HodFormActivity.this);
                        return;
                    }
                    final HashMap<String, String> jsonObject = new HashMap<>();
                    try {
                        jsonObject.put("name", hodFirstName.getText().toString().trim());
                        jsonObject.put("code", clzCode);
                        jsonObject.put("role_id", regRollId);
                        jsonObject.put("email", hodEmail.getText().toString().trim());
                        jsonObject.put("password", hodPassword.getText().toString().trim());
                        jsonObject.put("address", "NA");
                        jsonObject.put("branch_id", branchId);
                        jsonObject.put("institution_id", institutionID);
                        jsonObject.put("city", "NA");
                        jsonObject.put("qualification", edtQualification.getText().toString().trim());
                        jsonObject.put("mobile", hodMobile.getText().toString().trim());
                        jsonObject.put("gender_id", returnSelection(hodRgGender));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "After hash map");
                    Log.d(TAG, "Hash Map aprams--" + jsonObject);
                    new APIRequest(HodFormActivity.this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG, "HOD Activity--" + response);
                            Gson gson = new Gson();
                            SuccessResponsePojo successResponsePojo = gson.fromJson(response, SuccessResponsePojo.class);
                            if (successResponsePojo.getStatus().equals("1")) {
                                ToastUtils.displayToast(successResponsePojo.getMessage(), HodFormActivity.this);
                                if (selectionMode.equals("Teacher"))
                                    Utility.saveRole(HodFormActivity.this, Constants.TEACHER_ROLE);
                                else
                                    Utility.saveRole(HodFormActivity.this, Constants.HOD_ROLE);
                                Utility.saveLoginID(HodFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                                // Utility.saveUserID(HodFormActivity.this, successResponsePojo.getUser_id()); // saving user ID into pref
                                if (dialog.isShowing())
                                    dialog.dismiss();
                                Bundle b = new Bundle();
                                b.putString("mode_of_login", selectionMode);
                                Intent i = new Intent(HodFormActivity.this, TeachersDashboardActivity.class);
                                i.putExtras(b);
                                startActivity(i);
                                finish();
                            } else {
                                ToastUtils.displayToast(successResponsePojo.getError(), HodFormActivity.this);
                            }
                        }

                        @Override
                        public void onFailed(String message) {
                            ToastUtils.displayToast(Constants.something_went_wrong, HodFormActivity.this);
                            Log.d(TAG, "HOD Error message--" + message + "--jon respons--" + jsonObject);

                        }
                    });


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
