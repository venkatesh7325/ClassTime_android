package org.com.classmate.ui.activities.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.SuccessResponsePojo;
import org.com.classmate.model.UniversityList;
import org.com.classmate.model.admin.AdminDetails;
import org.com.classmate.ui.activities.common.IntroductionActivity;
import org.com.classmate.ui.activities.common.LoginActivity;
import org.com.classmate.ui.activities.students.StudentsListActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.FileUtils;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.ButterKnife;

public class AdminFormActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private CircularImageView imgProfile;
    MyCustomAdapter dataAdapter;
    private Spinner spinUniversity;
    private EditText edtEmail, edtCname, edtCCode, edtSelectBranch;
    private EditText edtPassword, edtCnfPwd;
    private EditText edtAddress, edtCity, edtPinCode, edtMobileNumber;
    private String TAG = AdminFormActivity.class.getSimpleName();
    String unvId = "1";
    List<BranchList> branchListsFromPref = null;
    HashMap<Integer, Integer> map = new HashMap<>();
    private static final int SELECT_FILE = 101;
    private static final int REQUEST_CAMERA = 102;
    Bitmap bitmapUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_admin_form);
        Utility.hideKeyBoard(this);// hide key board while lunching APP
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {

        imgProfile = (CircularImageView) findViewById(R.id.img_profile);
        spinUniversity = (Spinner) findViewById(R.id.spin_univ);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtCname = (EditText) findViewById(R.id.edt_cname);
        edtCCode = (EditText) findViewById(R.id.edt_ccode);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtCnfPwd = (EditText) findViewById(R.id.edt_cnf_password);
        edtAddress = (EditText) findViewById(R.id.edt_address);
        edtCity = (EditText) findViewById(R.id.edt_city);
        edtPinCode = (EditText) findViewById(R.id.edt_pincode);
        edtSelectBranch = (EditText) findViewById(R.id.edt_select_branch);
        edtMobileNumber = (EditText) findViewById(R.id.edt_mobile);
        edtSelectBranch.setOnClickListener(this);
        edtSelectBranch.setOnFocusChangeListener(this);
        edtSelectBranch.setInputType(InputType.TYPE_NULL);

        imgProfile.setOnClickListener(this);
        getUnviListFromPref();
        getBranchFromPref();
    }

    private void getUnviListFromPref() {
        try {
            String unvList = Utility.getUniversityList(this);
            Gson gson = new Gson();
            Type type = new TypeToken<List<UniversityList>>() {
            }.getType();
            List<UniversityList> universityLists = gson.fromJson(unvList, type);
            if (universityLists.size() > 0) {
                Log.d(TAG, "Unv-list_oncreate--" + universityLists.size() + "--fjk--" + universityLists.get(0).getUniversityName());
                List<String> list = new ArrayList<>();
                for (int i = 0; i < universityLists.size(); i++) {
                    list.add(universityLists.get(i).getUniversityName());
                }
                if (list.size() > 0)
                    setUnvToSpinner(list, universityLists);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUnvToSpinner(final List<String> universityLists, final List<UniversityList> unvList) {
        Log.d(TAG, "Unv-list set adapter--" + universityLists.size() + "--fjk--" + unvId);
        ArrayAdapter<String> arrayListAdapter = new ArrayAdapter<String>(AdminFormActivity.this, android.R.layout.simple_spinner_dropdown_item, universityLists);
        spinUniversity.setAdapter(arrayListAdapter);
        spinUniversity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "Unv-list Selected Item--" + unvList.get(i).getUniversityId());
                unvId = String.valueOf(unvList.get(i).getUniversityId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                unvId = "1";
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menuchanges, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.miClose:
                finish();
                break;
            // action with ID action_settings was selected
            case R.id.miDone:
                if (Utility.isConnectingToInternet(AdminFormActivity.this)) {
                    doRegistration();
                } else {
                    ToastUtils.displayToast(Constants.no_internet_connection, AdminFormActivity.this);
                }
                break;
            default:
                break;
        }

        return true;
    }

    private void doRegistration() {

        String clzName = edtCname.getText().toString().trim();
        String clzCode = edtCCode.getText().toString().trim();
        String clzNumber = edtMobileNumber.getText().toString();
        String clzCity = edtCity.getText().toString().trim();
        String clzAddress = edtAddress.getText().toString();
        String clzPincode = edtPinCode.getText().toString().trim();

        if (TextUtils.isEmpty(clzName)) {
            ToastUtils.displayToast("Please enter college name", this);
            return;
        }
        if (clzName.length() < 3) {
            ToastUtils.displayToast("College name minimum 3 char above", this);
            return;
        }

        if (TextUtils.isEmpty(clzCode)) {
            ToastUtils.displayToast("Please enter college code", this);
            return;
        }
        if (clzCode.length() < 3) {
            ToastUtils.displayToast("College code minimum 3 char above", this);
            return;
        }
        if (TextUtils.isEmpty(clzNumber)) {
            ToastUtils.displayToast("Please enter Mobile number", this);
            return;
        }
        if (clzNumber.length() != 10) {
            ToastUtils.displayToast("Please enter 10 digit Mobile number", this);
            return;
        }
        if (!Utility.isValidEmail(edtEmail.getText().toString())) {
            ToastUtils.displayToast("Please enter valid email", this);
            return;
        }
        String password = edtPassword.getText().toString().trim(), cnfpassword = edtCnfPwd.getText().toString().trim();
        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            ToastUtils.displayToast("Please enter Password", this);
            return;
        }
        if (TextUtils.isEmpty(edtCnfPwd.getText().toString())) {
            ToastUtils.displayToast("Please enter Confirm Password", this);
            return;
        }
        if (password.length() < 3) {
            ToastUtils.displayToast("Password minimum 3 char above", this);
            return;
        }
        if (!password.equals(cnfpassword)) {
            ToastUtils.displayToast("Password did not match", this);
            edtPassword.setText("");
            edtCnfPwd.setText("");
            return;
        }

        if (TextUtils.isEmpty(clzAddress)) {
            ToastUtils.displayToast("Please enter college address", this);
            return;
        }
        if (clzAddress.length() < 3) {
            ToastUtils.displayToast("College address minimum 3 char above", this);
            return;
        }
        if (TextUtils.isEmpty(clzCity)) {
            ToastUtils.displayToast("Please enter college city", this);
            return;
        }
        if (clzCity.length() < 3) {
            ToastUtils.displayToast("College city minimum 3 char above", this);
            return;
        }
        if ((TextUtils.isEmpty(clzPincode))) {
            ToastUtils.displayToast("Please Enter Pincode ", this);
            return;
        }
        if (clzPincode.length() != 6) {
            ToastUtils.displayToast("Please Enter Valid Pincode ", this);
            return;
        }

        /*{"message":{"name":"Muhilan","role_id":"1","email":"muhilan_admin@gmail.com","mobile":"1234567890","password":"123456",
        "address":"Test","city":"Test","pincode":"560001","college_code":"Test001","university_id":1,"branch":"1,2,3,4"}}*/
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            hashMap.put("name", clzName);
            hashMap.put("college_code", clzCode);
            hashMap.put("role_id", Constants.ADMIN_ROLE);
            hashMap.put("email", edtEmail.getText().toString().trim());
            hashMap.put("password", password);
            hashMap.put("address", clzAddress);
            hashMap.put("pincode", clzPincode);
            hashMap.put("university_id", unvId);
            hashMap.put("city", clzCity);
            hashMap.put("branch", "1,2,3,4,6,10,11");
            hashMap.put("mobile", clzNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Admin Activity HASHAMP--" + hashMap);
        new APIRequest(this).postStringRequest(ApiConstants.REGISTER, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Admin Activity--" + response);
                Gson gson = new Gson();
                SuccessResponsePojo successResponsePojo = gson.fromJson(response, SuccessResponsePojo.class);
                if (successResponsePojo.getStatus().equals("1")) {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), AdminFormActivity.this);
                    //  Utility.saveUserID(AdminFormActivity.this, successResponsePojo.getId()); // saving user ID into pref
                    saveImageIntoSdCard();
                    Bundle b = new Bundle();
                    b.putString("mode_of_login", "Admin");
                    Intent i = new Intent(AdminFormActivity.this, LoginActivity.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), AdminFormActivity.this);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, AdminFormActivity.this);
                Log.d(TAG, "Admin Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }
    void saveImageIntoSdCard() {
        try {
            if (bitmapUser == null) {
                Log.d(TAG, "Camera Bitmap NULL");
                return;
            }
            boolean imgSaveFlag = FileUtils.saveImageIntoSdcard(AdminFormActivity.this, bitmapUser, getResources().getString(R.string.app_name) + "_" + Utility.getUserID(AdminFormActivity.this) + ".jpg");
            Log.d(TAG, "Camera Bitmap--" + imgSaveFlag);
            if (imgSaveFlag)
                imgProfile.setImageBitmap(bitmapUser);
            else
                ToastUtils.displayToast("Image not saved in SD CARD", AdminFormActivity.this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_select_branch:
                popupToShowDiscounts();
                break;
            case R.id.img_profile:
                selectImage();
                break;
            default:
                break;
        }
    }


    @Override
    public void onFocusChange(View view, boolean b) {
    }

    private void popupToShowDiscounts() {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(new ContextThemeWrapper(AdminFormActivity.this, R.style.Animation));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_select_branch_list);
            dialog.setTitle("Select Branch");
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (!dialog.isShowing())
                dialog.show();
            ImageView imageClose = (ImageView) dialog.findViewById(R.id.close);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
            final ListView lvBranches = (ListView) dialog.findViewById(R.id.listView1);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_save_branch_list);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* String s = "";
                    if (map.size() > 0) {
                        for (Map.Entry<Integer, Integer> map1 : map.entrySet()) {
                            ToastUtils.displayToast("Checked branch Value--" + map1.getValue(), AdminFormActivity.this);
                            if (map.size() == 1)
                                s = map1.getValue().toString();
                            else
                                s = map1.getValue().toString() + ",";
                            ToastUtils.displayToast("Checked values string--" + s, AdminFormActivity.this);
                        }
                        ToastUtils.displayToast("Checked branch list--" + map.size(), AdminFormActivity.this);*/
                    if (dialog.isShowing())
                        dialog.dismiss();
                    /*} else {
                        ToastUtils.displayToast("Please select at least one branch", AdminFormActivity.this);
                    }*/
                }
            });
            if (branchListsFromPref != null && branchListsFromPref.size() > 0)
                displayListView(lvBranches);
            else
                ToastUtils.displayToast("No branch to select", AdminFormActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayListView(ListView listView) {
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.snippet_select_branch, branchListsFromPref);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                BranchList branchList = (BranchList) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + branchList.getBranchName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<BranchList> {

        private List<BranchList> brnachList;
        List<Integer> checkedPositions = new ArrayList<Integer>();

        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<BranchList> countryList) {
            super(context, textViewResourceId, countryList);
            this.brnachList = countryList;
            Log.d(TAG, "Brach list--" + brnachList.size());
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.snippet_select_branch, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);
                holder.name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    }
                });

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        // Toast.makeText(getApplicationContext(), "Row " + position + " is checked", Toast.LENGTH_SHORT).show();
                        if (cb.isChecked()) {
                            map.put(position, brnachList.get(position).getBranchId());
                            // when checkbox is checked
                            //   Toast.makeText(getApplicationContext(), "Add Size " + map.size(), Toast.LENGTH_SHORT).show();
                        } else {
                            // checkbox is unchecked
                            // Toast.makeText(getApplicationContext(), "Remove Row " + map.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            BranchList country = brnachList.get(position);
            holder.code.setText(" (" + country.getBranchId() + ")");
            holder.name.setText(country.getBranchName());
            return convertView;

        }

        @Override
        public int getCount() {
            return brnachList.size();
        }
    }

    private void selectImage() { //Choose option to capture image from camera or gallery
        try {
            final CharSequence[] items = {"Camera", "Gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AdminFormActivity.this);
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
}
