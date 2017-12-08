package org.com.classmate.ui.activities.teacher;

import android.app.DatePickerDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.interfaces.SubjectListResponse;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.TaskModule.creationTask.TaskCreation;
import org.com.classmate.model.subjectList.SubjectList;
import org.com.classmate.model.subjectList.SubjectListDetails;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;


import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;


public class TeachersTaskAssignActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private final String TAG = TeachersTaskAssignActivity.this.getClass().getSimpleName();

    EditText edtStartDate;
    EditText edtEndDate;

    private int PICK_PDF_REQUEST = 1;//Pdf request code
    Button btnUpload;
    CustomTextViewMedium tvFileNames;
    ImageView imgDocs;
    LinearLayout llDoc;
    EditText edtTaskName;
    EditText edtTaskDesc;
    EditText edtTaskSubject, edtDept;
    Spinner spYear;
    Spinner spSemister;

    List<BranchList> branchListsFromPref = null;
    private String branchId = "";
    String subjectId = "";
    List<SubjectList> subjectLists = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_task_layout);
        Utility.hideKeyBoard(this);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getBranchFromPref();
        init();
    }

    private void init() {
        edtStartDate = (EditText) findViewById(R.id.txtStartDate);
        edtEndDate = (EditText) findViewById(R.id.txtEndDate);

        edtTaskName = (EditText) findViewById(R.id.edt_taskName);
        edtTaskDesc = (EditText) findViewById(R.id.edt_taskDesc);
        edtTaskSubject = (EditText) findViewById(R.id.edt_subject);

        edtDept = (EditText) findViewById(R.id.edt_dept);
        edtDept.setOnClickListener(this);
        edtDept.setInputType(InputType.TYPE_NULL);
        edtDept.setCursorVisible(false);
        edtDept.setOnFocusChangeListener(this);

        edtTaskSubject.setOnClickListener(this);
        edtTaskSubject.setInputType(InputType.TYPE_NULL);
        edtTaskSubject.setCursorVisible(false);
        edtTaskSubject.setOnFocusChangeListener(this);


        edtEndDate.setOnClickListener(this);
        edtStartDate.setOnClickListener(this);
        edtStartDate.setInputType(InputType.TYPE_NULL);
        edtStartDate.setCursorVisible(false);
        edtStartDate.setOnFocusChangeListener(this);
        edtEndDate.setInputType(InputType.TYPE_NULL);
        edtEndDate.setCursorVisible(false);
        edtEndDate.setOnFocusChangeListener(this);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(this);
        tvFileNames = (CustomTextViewMedium) findViewById(R.id.tv_fileName);
        imgDocs = (ImageView) findViewById(R.id.doc_images);
        llDoc = (LinearLayout) findViewById(R.id.ll_getFile);
        spYear = (Spinner) findViewById(R.id.sp_choose_years);
        spSemister = (Spinner) findViewById(R.id.sp_choose_semi);
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
                if (Utility.isConnectingToInternet(this))
                    createTaskForStudent();
                else
                    ToastUtils.displayToast(Constants.no_internet_connection, this);
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtStartDate:
                showDialog(edtStartDate, "Select Start Date");
                break;
            case R.id.txtEndDate:
                showDialog(edtEndDate, "Select End Date");
                break;
            case R.id.btn_upload:
                showFileChooser();
                break;
            case R.id.edt_dept:
                loadBranchAndSelect();
                break;

            case R.id.edt_subject:
                if (!branchId.isEmpty())
                    loadSubjects();
                else
                    ToastUtils.displayToast("Please Select your branch", TeachersTaskAssignActivity.this);
                break;
        }
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
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeachersTaskAssignActivity.this, android.R.layout.simple_list_item_1, branchNameList);
        AlertDialog.Builder builder = new AlertDialog.Builder(TeachersTaskAssignActivity.this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                edtDept.setText(branchListsFromPref.get(item).getBranchName());
                edtDept.setTag(branchListsFromPref.get(item).getBranchId());
                Log.d(TAG, "Branch Name " + branchListsFromPref.get(item).getBranchName());
                Log.d(TAG, "Branch Id " + branchListsFromPref.get(item).getBranchId());
                branchId = String.valueOf(branchListsFromPref.get(item).getBranchId());

                if (getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
                    ToastUtils.displayToast("Please select year", TeachersTaskAssignActivity.this);
                    return;
                }
                if (getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSemister.getSelectedItem().toString())) {
                    ToastUtils.displayToast("Please select semester", TeachersTaskAssignActivity.this);
                    return;
                }
                APIRequest.callApiToGetSubjectList(TeachersTaskAssignActivity.this,
                        /*Utility.getInstitutionId(TeachersTaskAssignActivity.this)*/"2", branchId, Constants.getYearId(spYear.getSelectedItem().toString()), Constants.getYearId(spSemister.getSelectedItem().toString()), new SubjectListResponse() {
                            @Override
                            public void subjectListResponse(SubjectListDetails subjectListDetails) {
                                if (subjectListDetails != null && subjectListDetails.getSubjectList() != null)
                                    subjectLists = subjectListDetails.getSubjectList();
                            }
                        });
                dialog.cancel();

            }
        });
        final AlertDialog dialog = builder.create();
        if (branchListsFromPref.size() != 0)
            dialog.show();

    }


    private void loadSubjects() {
        try {
            List<String> branchNameList = new ArrayList<>();
            for (int i = 0; i < subjectLists.size(); i++) {
                branchNameList.add(subjectLists.get(i).getName());
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(TeachersTaskAssignActivity.this, android.R.layout.simple_list_item_1, branchNameList);
            AlertDialog.Builder builder = new AlertDialog.Builder(TeachersTaskAssignActivity.this);
            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    SubjectListDetails getStudentsListPojo = new SubjectListDetails();
                    edtTaskSubject.setText(subjectLists.get(item).getName());
                    edtTaskSubject.setTag(subjectLists.get(item).getId());
                    Log.d(TAG, "edtTaskSubject Name " + subjectLists.get(item).getName());
                    Log.d(TAG, "edtTaskSubject Id " + subjectLists.get(item).getId());
                    subjectId = String.valueOf(subjectLists.get(item).getId());
                    dialog.cancel();

                }
            });
            final AlertDialog dialog = builder.create();
            if (subjectLists.size() != 0)
                dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showDialog(final EditText textView, String title) {
        //  2017-11-04
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        final Calendar newCalendar = Calendar.getInstance();
        final DatePickerDialog fromDatePickerDialog = new
                DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {


            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newCalendar.set(year, monthOfYear, dayOfMonth);
                //  Logger.logV("teh date is", "the date and time is" + dateFormatter.format(newCalendar.getTime()));
                textView.setText(dateFormatter.format(newCalendar.getTime()));

            }


        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.setCancelable(false);
        fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        fromDatePickerDialog.setTitle(title);
        fromDatePickerDialog.show();

    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri fileUri = data.getData();
            if (fileUri != null)
                showFileNameAndExtension(fileUri);
        }
    }

    // get file name and extension from uri
    void showFileNameAndExtension(Uri fileUri) {
        try {
            Log.d(TAG, "File name : " + Utility.getFileName(TeachersTaskAssignActivity.this, fileUri));
            Log.d(TAG, "File Extension: " + Utility.getMimeType(TeachersTaskAssignActivity.this, fileUri));
            llDoc.setVisibility(View.VISIBLE);
            tvFileNames.setText(Utility.getFileName(TeachersTaskAssignActivity.this, fileUri));
            Utility.setDocTypeImage(this, imgDocs, Utility.getMimeType(TeachersTaskAssignActivity.this, fileUri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTaskForStudent() {
        try {
            String taskName = edtTaskName.getText().toString().trim();
            String taskDesc = edtTaskDesc.getText().toString().trim();
            String taskStartDate = edtStartDate.getText().toString().trim();
            String taskEndDate = edtEndDate.getText().toString().trim();
            if (taskName.isEmpty()) {
                ToastUtils.displayToast("Please Enter Task name  ", this);
                return;
            }
            if (taskName.length() < 3) {
                ToastUtils.displayToast("Please Enter Task name above 3char ", this);
                return;
            }
            if (taskDesc.isEmpty()) {
                ToastUtils.displayToast("Please Enter Task Description  ", this);
                return;
            }
            if (taskDesc.length() < 3) {
                ToastUtils.displayToast("Please Enter Task Description above 3char ", this);
                return;
            }
            if (taskStartDate.isEmpty()) {
                ToastUtils.displayToast("Please select Task Start date ", this);
                return;
            }
            if (taskEndDate.isEmpty()) {
                ToastUtils.displayToast("Please select Task End date ", this);
                return;
            }

            if (getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
                ToastUtils.displayToast("Please select year", TeachersTaskAssignActivity.this);
                return;
            }
            if (getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSemister.getSelectedItem().toString())) {
                ToastUtils.displayToast("Please select semester", TeachersTaskAssignActivity.this);
                return;
            }
            final HashMap<String, String> jsonObject = new HashMap<>();
            try {
                //Constants.getYearId(spYear.getSelectedItem().toString()), Constants.getYearId(spSem.getSelectedItem().toString()
                jsonObject.put("title", taskName);
                jsonObject.put("description", taskDesc);
                jsonObject.put("file", "File.doc");
                jsonObject.put("branch_id", branchId);
                jsonObject.put("year", Constants.getYearId(spYear.getSelectedItem().toString()));
                jsonObject.put("semester", Constants.getYearId(spSemister.getSelectedItem().toString()));
                jsonObject.put("teacher_id", String.valueOf(Utility.getUserID(TeachersTaskAssignActivity.this)));
                jsonObject.put("start_date", taskStartDate);
                jsonObject.put("end_date", taskEndDate);
                jsonObject.put("subject_id", subjectId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d(TAG, "After hash map");
            Log.d(TAG, "Hash Map aprams--" + jsonObject);
            new APIRequest(this).postStringRequest(ApiConstants.TASK_CREATION, jsonObject, new RequestCallBack() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, "TASK Activity--" + response);
                    Gson gson = new Gson();
                    TaskCreation taskCreation = gson.fromJson(response, TaskCreation.class);
                    if (taskCreation.getStatus().equals("success")) {
                        ToastUtils.displayToast(taskCreation.getMessage(), TeachersTaskAssignActivity.this);
                        finish();
                    } else {
                        ToastUtils.displayToast(taskCreation.getMessage(), TeachersTaskAssignActivity.this);
                    }
                }

                @Override
                public void onFailed(String message) {
                    ToastUtils.displayToast(Constants.something_went_wrong, TeachersTaskAssignActivity.this);
                    Log.d(TAG, "TASK Error message--" + message + "--jon respons--" + jsonObject);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.txtStartDate:
                if (!hasFocus) {
                    return;
                }
                showDialog(edtStartDate, "Select Start Date");
                break;
            case R.id.txtEndDate:
                if (!hasFocus) {
                    return;
                }
                showDialog(edtEndDate, "Select End Date");
                break;
            case R.id.edt_dept:
                if (!hasFocus) {
                    return;
                }
                loadBranchAndSelect();
                break;
            case R.id.edt_subject:
                if (!hasFocus) {
                    return;
                }
                loadSubjects();
                break;
            default:
                break;
        }


    }
}
