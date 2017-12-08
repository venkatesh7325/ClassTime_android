package org.com.classmate.ui.activities.students;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.GetClz.GetDetailsFromClzRegId.GetCollegeIDPojo;
import org.com.classmate.model.SuccessResponsePojo;
import org.com.classmate.model.admin.AdminDetails;
import org.com.classmate.model.common.Branches;
import org.com.classmate.ui.activities.common.LoginActivity;
import org.com.classmate.ui.activities.hod.HodFormActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.FileUtils;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class StudentFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    CustomTextViewMedium edtCvcode;
    CustomEditTextMedium edtStudentRollNumber;
    CustomEditTextMedium edtStudentFirstName;
    CustomEditTextMedium edtStudentLastName;
    CustomEditTextMedium edtStudentEmail;
    CustomEditTextMedium edtStudentMobile;
    CustomEditTextMedium edtStudentPassWord;
    CustomEditTextMedium edtStudentConfrmPassWord;
    CustomEditTextMedium edtStudentAddress;
    CustomEditTextMedium edtStudentCity;
    Spinner spYear;
    Spinner spSemester;
    Spinner spBranch;
    RadioGroup radioGroupStntGender;
    Button btnSaveStudentDetails;
    CustomEditTextMedium edtBranch;
    private String TAG = "Student registration";
    List<BranchList> branchListsFromPref = null;
    private String branchId = "";
    String institutionID = "";
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 102;
    Bitmap bitmapUser;
    private CircularImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_form);
        Utility.hideKeyBoard(this);// hide key board while lunching APP
        getBranchFromPref();
        setToolBar();
        initBinds();
    }

    void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Student Registration");
        toolbar.setTitleTextColor(getResources().getColor(R.color.app_white_buttons_bg_color));
    }

    void initBinds() {
        imgProfile = (CircularImageView) findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(this);
        edtCvcode = (CustomTextViewMedium) findViewById(R.id.edt_cvcode);
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
        btnSaveStudentDetails.setOnClickListener(this);
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

    void callAPIToGetCollegeDetails(String whichApi, String postKey, final String postValues) {
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
                        ToastUtils.displayToast(getCollegeIDPojo.getCollegeDetails().getInstitutionName(), StudentFormActivity.this);
                        edtCvcode.setText(postValues);
                        institutionID = String.valueOf(getCollegeIDPojo.getCollegeDetails().getInstitutionId());
                        branchId = String.valueOf(getCollegeIDPojo.getCollegeDetails().getBranchId());
                        if (branchListsFromPref == null)
                            return;
                        for (int i = 0; i < branchListsFromPref.size(); i++) {
                            if (branchId.equals(String.valueOf(branchListsFromPref.get(i).getBranchId()))) {
                                edtBranch.setText(branchListsFromPref.get(i).getBranchName());
                            }
                        }
                        Log.d(TAG, "Branch ID--" + getCollegeIDPojo.getCollegeDetails().getBranchId());
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

    private void doRegistration() {

        String stFirstName = edtStudentFirstName.getText().toString().trim();
        String stLastName = edtStudentLastName.getText().toString().trim();
        String code = edtCvcode.getText().toString().trim();
        String studentRollNumber = edtStudentRollNumber.getText().toString().trim();
        String stEmail = edtStudentEmail.getText().toString().trim();
        String stCity = edtStudentCity.getText().toString().trim();
        String stAdd = edtStudentAddress.getText().toString().trim();
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

/*{ "name": "Christ Student","roll_number":"9999999999", "email": "christ_student@gmail.com",
"gender_id":"1", "password": "123456", "branch_id": "1", "address": "address",
 "city": "city", "code": "CRITHD5", "role_id": "4", "batch_id": "1",
 "qualification": "BE", "mobile": "7878787877", "institution_id":"1" }*/

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
        /*Intent intent = new Intent(this, StudentDayActivity.class);
        startActivity(intent);*/
        new APIRequest(this).postStringRequest(ApiConstants.REGISTER, jsonObject, new RequestCallBack() {
            @Override
            public void onResponse(String jsonObject) {
                ToastUtils.displayToast(jsonObject, StudentFormActivity.this);
                Log.d(TAG, "Student Activity--" + jsonObject);
                Gson gson = new Gson();
                SuccessResponsePojo successResponsePojo = gson.fromJson(jsonObject, SuccessResponsePojo.class);
                if (successResponsePojo.getStatus().equals("1")) {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), StudentFormActivity.this);
                    //  Utility.saveUserID(AdminFormActivity.this, successResponsePojo.getId()); // saving user ID into pref
                    saveImageIntoSdCard();
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
    }

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
            case R.id.img_profile:
                selectImage();
                break;
            case R.id.btn_student_save:
                doRegistration();
                break;
            case R.id.edt_cvcode:
                showAlertDialog();
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
        /*InputMethodManager imm = (InputMethodManager) HodFormActivity.this.getSystemService(INPUT_METHOD_SERVICE);
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

    private void selectImage() { //Choose option to capture image from camera or gallery
        try {
            final CharSequence[] items = {"Camera", "Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (items[item].equals("Camera")) {
                        cameraIntent();

                    } else if (items[item].equals("Gallery")) {
                        galleryIntent();

                    }
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cameraIntent() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void galleryIntent() {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);//
            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        try {
            if (data != null) {
                try {
                    bitmapUser = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    Log.d(TAG, "Gallery Bitmap--" + bitmapUser);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            imgProfile.setImageBitmap(bitmapUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onCaptureImageResult(Intent data) {
        try {
            if (data != null && data.getExtras() != null && data.getExtras().get("data") != null) {
                bitmapUser = (Bitmap) data.getExtras().get("data");
                if (bitmapUser == null) {
                    Log.d(TAG, "Camera Bitmap NULL");
                    return;
                }
                imgProfile.setImageBitmap(bitmapUser);
            } else
                Log.d(TAG, "DATA NULL");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveImageIntoSdCard() {
        try {
            if (bitmapUser == null) {
                Log.d(TAG, "Camera Bitmap NULL");
                return;
            }
            boolean imgSaveFlag = FileUtils.saveImageIntoSdcard(StudentFormActivity.this, bitmapUser, getResources().getString(R.string.app_name) + "_" + Utility.getUserID(StudentFormActivity.this) + ".jpg");
            Log.d(TAG, "Camera Bitmap--" + imgSaveFlag);
            if (imgSaveFlag)
                imgProfile.setImageBitmap(bitmapUser);
            else
                ToastUtils.displayToast("Image not saved in SD CARD", StudentFormActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
