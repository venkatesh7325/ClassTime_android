package org.com.classmate.ui.activities.hod;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.GetClz.GetDetailsFromClzRegId.GetCollegeIDPojo;
import org.com.classmate.model.SuccessResponsePojo;
import org.com.classmate.ui.activities.common.LoginActivity;
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
import java.util.HashMap;
import java.util.List;

public class HodFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private static final String TAG = "HodFormActivity";
    CustomEditTextMedium edtBranch;
    EditText hodFirstName;
    EditText hodLastName;
    EditText hodEmail;
    EditText hodMobile;
    RadioGroup hodRgGender;
    EditText hodPassword;
    EditText hodCnfmPassword;
    EditText hodAddress;
    EditText hodCity;
    EditText edtQualification;
    CustomTextViewMedium collegeVerficationCode;
    String selectionMode = "";
    Button btnSave;
    String regRollId = "";
    String institutionID = "";
    String branchId = "";
    List<BranchList> branchListsFromPref = null;
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 102;
    Bitmap bitmapUser;
    private CircularImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_form);
        Utility.hideKeyBoard(this);// hide key board while lunching APP
        getBranchFromPref();
        getDateFromPreviousActivity();
        initToolBar();
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
        imgProfile = (CircularImageView) findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(this);
        edtBranch = (CustomEditTextMedium) findViewById(R.id.edt_select_branch);
        edtBranch.setOnClickListener(this);
        edtBranch.setOnFocusChangeListener(this);
        edtBranch.setInputType(InputType.TYPE_NULL);
        collegeVerficationCode = (CustomTextViewMedium) findViewById(R.id.edt_cvcode);
        collegeVerficationCode.setOnClickListener(this);
        if (!"HOD".equals(selectionMode)) {
            collegeVerficationCode.setHint("Enter HOD verification code");
        }
        hodFirstName = (EditText) findViewById(R.id.edt_hod_fname);
        hodLastName = (EditText) findViewById(R.id.edt_hod_lname);
        hodEmail = (EditText) findViewById(R.id.edt_email);
        hodMobile = (EditText) findViewById(R.id.edt_mobile);
        hodAddress = (EditText) findViewById(R.id.edt_address);
        hodPassword = (EditText) findViewById(R.id.edt_password);
        hodCnfmPassword = (EditText) findViewById(R.id.edt_cnf_password);
        hodCity = (EditText) findViewById(R.id.edt_city);
        hodRgGender = (RadioGroup) findViewById(R.id.rg_gender);
        btnSave = (Button) findViewById(R.id.btn_save);
        edtQualification = (EditText) findViewById(R.id.edt_hod_qualification);
        btnSave.setOnClickListener(this);
    }

    void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            if (selectionMode.equals("HOD"))
                getSupportActionBar().setTitle("HOD Registration");
            else
                getSupportActionBar().setTitle("Teacher Registration");
        }
    }

    void esetInputTypes() {
        hodFirstName.setInputType(InputType.TYPE_NULL);
        hodLastName.setInputType(InputType.TYPE_NULL);
        hodMobile.setInputType(InputType.TYPE_NULL);

        hodEmail.setInputType(InputType.TYPE_NULL);
        hodAddress.setInputType(InputType.TYPE_NULL);
        hodPassword.setInputType(InputType.TYPE_NULL);

        hodCnfmPassword.setInputType(InputType.TYPE_NULL);
        hodCity.setInputType(InputType.TYPE_NULL);
        hodMobile.setInputType(InputType.TYPE_NULL);
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

    private void doRegistration() {
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
                        //  Utility.saveUserID(AdminFormActivity.this, successResponsePojo.getId()); // saving user ID into pref
                        saveImageIntoSdCard();
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
    }

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
            case R.id.btn_save:
                if (Utility.isConnectingToInternet(HodFormActivity.this)) {
                    Log.d(TAG, "Registration");
                    doRegistration();
                } else {
                    ToastUtils.displayToast(Constants.no_internet_connection, HodFormActivity.this);
                }
                break;
            case R.id.edt_cvcode:
                showAlertDialog();
                break;
            case R.id.edt_select_branch:
                if (selectionMode.equals("HOD"))
                    loadBranchAndSelect();
                break;
            case R.id.img_profile:
                selectImage();
                break;
            default:
                break;

        }

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
    }

    void callAPIToGetCollegeDetails(String whichApi, String postKey, String postValues) {
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
                        ToastUtils.displayToast(getCollegeIDPojo.getCollegeDetails().getInstitutionName(), HodFormActivity.this);
                        institutionID = String.valueOf(getCollegeIDPojo.getCollegeDetails().getInstitutionId());
                        if (selectionMode.equals("Teacher"))
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
            boolean imgSaveFlag = FileUtils.saveImageIntoSdcard(HodFormActivity.this, bitmapUser, getResources().getString(R.string.app_name) + "_" + Utility.getUserID(HodFormActivity.this) + ".jpg");
            Log.d(TAG, "Camera Bitmap--" + imgSaveFlag);
            if (imgSaveFlag)
                imgProfile.setImageBitmap(bitmapUser);
            else
                ToastUtils.displayToast("Image not saved in SD CARD", HodFormActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
