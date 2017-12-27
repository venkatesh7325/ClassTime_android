package org.com.classmate.ui.activities.teacher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.R;
import org.com.classmate.adapter.TimeTableAdapter;
import org.com.classmate.interfaces.CreateTimeTable;
import org.com.classmate.interfaces.ShowTimetable;
import org.com.classmate.interfaces.SubjectListResponse;
import org.com.classmate.interfaces.TimeSelected;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.UniversityList;
import org.com.classmate.model.showTimetable.ShowTimetableDetails;
import org.com.classmate.model.subjectList.SubjectList;
import org.com.classmate.model.subjectList.SubjectListDetails;
import org.com.classmate.model.teacher.TimeTable;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TeachersTimeTableActivity extends AppCompatActivity implements TimeSelected, SubjectListResponse, CreateTimeTable, ShowTimetable {

    /**
     * Intialization
     */
    @Bind(R.id.textWeekDate)
    TextView txtDate;

    /**
     * constants
     **/
    String monthArray[] = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    GridView mImageGrid;
    double time = 0.0;
    List<SubjectList> subjectList = new ArrayList<SubjectList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_time_table);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mImageGrid = (GridView) findViewById(R.id.timeTableGrid);

        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        init();
        ArrayList<TimeTable> listtable = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        time = 8.30;
    /*    for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                TimeTable item = new TimeTable();
                if (i == 0) {
                    if (j == 0) {
                        item.setEnable("text");
                        item.setValue("UTC");
                    } else {
                        item.setEnable("text");
                        String day = Constants.getDay(String.valueOf(j));
                        item.setValue(day);
                        // c.add(Calendar.DAY_OF_MONTH, 1);
                    }
                } else {
                    if (j == 0) {
                        item.setEnable("text");
                        item.setValue(String.valueOf(time) + " :00");
                    } else {
                        item.setEnable("view");
                        item.setValue(" ");
                    }

                }
                listtable.add(item);
            }
            if (time == 12.3) {
                time = 14.00;
            } else
                time++;
                }*/


/*
        TimeTableAdapter adapter = new TimeTableAdapter(this, listtable, TeachersTimeTableActivity.this);
        mImageGrid.setAdapter(adapter);*/
        APIRequest.showTimeTable(TeachersTimeTableActivity.this, "", "", TeachersTimeTableActivity.this);


    }

    private void init() {
        txtDate.setText("" + getDateofTheWeek());

    }

    private String getDateofTheWeek() {


        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c1.setFirstDayOfWeek(Calendar.MONDAY);

        int startDate = c.get(Calendar.DAY_OF_MONTH);
        int endDate = c.get(Calendar.DAY_OF_MONTH) + 5;
        int year = c.get(Calendar.YEAR);
        String month = monthArray[c.get(Calendar.MONTH)];

        c1.add(Calendar.DAY_OF_MONTH, 5);

        if (c1.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
            if (c1.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                return month + " " + startDate + " - " + endDate + " " + year;
            } else {
                return month + " " + startDate + " - " + endDate + " " + monthArray[c1.get(Calendar.MONTH)] + " " + year;
            }
        } else {
            return month + " " + startDate + " " + c.get(Calendar.YEAR) + " - " + endDate + " " + monthArray[c1.get(Calendar.MONTH)] + " " + c1.get(Calendar.YEAR);
        }
    }

    private int getDate(Calendar c) {
        int startDate = c.get(Calendar.DAY_OF_MONTH);
        return startDate;
    }

    private String getMonth(Calendar c) {
        String month = monthArray[c.get(Calendar.MONTH)];
        return month;
    }

    private void popupToGetStudentsList(final Context context, final int position) {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.Animation));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.create_time_table);
            final Spinner university, branch, year, semister, subject;
            university = (Spinner) dialog.findViewById(R.id.university);
            branch = (Spinner) dialog.findViewById(R.id.department);
            year = (Spinner) dialog.findViewById(R.id.year);
            semister = (Spinner) dialog.findViewById(R.id.semister);
            subject = (Spinner) dialog.findViewById(R.id.subject);
            subject.setVisibility(View.GONE);
            final Button btnSubmit = (Button) dialog.findViewById(R.id.btn_submit);
            String universities = Utility.getUniversityList(context);
            Gson gson = new Gson();
            Type type = new TypeToken<List<UniversityList>>() {
            }.getType();
            List<UniversityList> yourList = gson.fromJson(universities, type);
            UniversityList universityBeen = new UniversityList();
            universityBeen.setUniversityId(0);
            universityBeen.setUniversityName("Choose a University");
            yourList.add(universityBeen);
            ArrayAdapter<UniversityList> adapter =
                    new ArrayAdapter<UniversityList>(context, android.R.layout.simple_spinner_dropdown_item, yourList);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            university.setSelection(0);
            university.setAdapter(adapter);
            String branchList = Utility.getBranchListList(context);
            Type type1 = new TypeToken<List<BranchList>>() {
            }.getType();
            List<BranchList> branchLists = gson.fromJson(branchList, type1);
            BranchList branchListPojo = new BranchList();
            branchListPojo.setBranchId(0);
            branchListPojo.setBranchName("Select Department");
            branchLists.add(branchListPojo);
            ArrayAdapter<BranchList> adapter1 =
                    new ArrayAdapter<BranchList>(context, android.R.layout.simple_spinner_dropdown_item, branchLists);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            branch.setSelection(0);
            branch.setAdapter(adapter1);
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
            //university.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) context);
            UniversityList universitys = (UniversityList) university.getSelectedItem();
            BranchList branchListsss = (BranchList) branch.getSelectedItem();
            university.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (context.getResources().getString(R.string.Spiner_college_prompt).equalsIgnoreCase(university.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_dept_prompt).equalsIgnoreCase(branch.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(year.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(semister.getSelectedItem().toString())) {
                        return;
                    }
                    apiCall(context, university, branch, year.getSelectedItem().toString(), semister.getSelectedItem().toString(), subject,btnSubmit);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            semister.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (context.getResources().getString(R.string.Spiner_college_prompt).equalsIgnoreCase(university.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_dept_prompt).equalsIgnoreCase(branch.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(year.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(semister.getSelectedItem().toString())) {
                        return;
                    }
                    apiCall(context, university, branch, year.getSelectedItem().toString(), semister.getSelectedItem().toString(), subject,btnSubmit);

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    BranchList branchLists = (BranchList) parent.getSelectedItem();
                    String idgk = String.valueOf(branchLists.getBranchId());
                    Log.v("the ids is", "teh selected id is" + idgk);
                    if (context.getResources().getString(R.string.Spiner_college_prompt).equalsIgnoreCase(university.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_dept_prompt).equalsIgnoreCase(branch.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(year.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(semister.getSelectedItem().toString())) {
                        return;
                    }
                    apiCall(context, university, branch, year.getSelectedItem().toString(), semister.getSelectedItem().toString(), subject,btnSubmit);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (context.getResources().getString(R.string.Spiner_college_prompt).equalsIgnoreCase(university.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_dept_prompt).equalsIgnoreCase(branch.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(year.getSelectedItem().toString())) {
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(semister.getSelectedItem().toString())) {
                        return;
                    }
                    apiCall(context, university, branch, year.getSelectedItem().toString(), semister.getSelectedItem().toString(), subject,btnSubmit);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Log.v("the university is", "the university is" + university.getSelectedItemId() + university.getSelectedItem());
            btnSubmit.setVisibility(View.GONE);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    createTableRequest(context, branch, year.getSelectedItem().toString(), semister.getSelectedItem().toString(), subject, "2", String.valueOf(Constants.setPeriod(position)), String.valueOf(Constants.setDay(position)));
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setSubjectSpinner(Button submit,Spinner subject, Context context, SubjectListDetails subjectListDetails) {
        subjectList = subjectListDetails.getSubjectList();
        if (subjectList!=null && subjectList.size()!=0) {
            subject.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
        }
        else
        {
            submit.setVisibility(View.GONE);
            subject.setVisibility(View.GONE);
            ToastUtils.displayToast("Subject list is empty please try with other department",context);
        }
        Log.v("the subject list is", "the subject list is");
        ArrayAdapter<SubjectList> adapter =
                new ArrayAdapter<SubjectList>(context, android.R.layout.simple_spinner_dropdown_item, subjectList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject.setSelection(0);
        subject.setAdapter(adapter);
    }

    public void apiCall(final Context context, Spinner universityId, Spinner branchId, String year, String semister, final Spinner subject, final Button submitButton) {
        if (Utility.isConnectingToInternet(context)) {
            BranchList branch = (BranchList) branchId.getSelectedItem();
            Log.v("the branch id is", "the branch id is" + branch.getBranchId());
            UniversityList university = (UniversityList) universityId.getSelectedItem();
            Log.v("the branch id is", "the branch id is" + university.getUniversityId());
            APIRequest.callApiToGetSubjectList(context, String.valueOf(university.getUniversityId()), String.valueOf(branch.getBranchId()), Constants.getYearId(year), Constants.getYearId(semister), new SubjectListResponse() {
                @Override
                public void subjectListResponse(SubjectListDetails subjectListDetails) {
                    setSubjectSpinner(submitButton,subject, context, subjectListDetails);
                }
            });
        } else
            ToastUtils.displayToast(Constants.no_internet_connection, context);

    }

    public void createTableRequest(final Context context, Spinner branch, String year, String semister, final Spinner subject, String teacher, String period, String day) {
        if (Utility.isConnectingToInternet(context)) {
            BranchList branchId = (BranchList) branch.getSelectedItem();
            SubjectList subjectId = (SubjectList) subject.getSelectedItem();

            APIRequest.createTimeTable(context, String.valueOf(branchId.getBranchId()), Constants.getYearId(year), Constants.getYearId(semister), teacher, period, subjectId.getId().toString(), day, new CreateTimeTable() {
                @Override
                public void createTimeTable(String result) {
                    try {
                        JSONObject jsonObj = new JSONObject(result);
                        String status = jsonObj.getString("status");
                        String message = jsonObj.getString("message");
                        if (status.equalsIgnoreCase("1"))
                            ToastUtils.displayToast(message, context);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.v("the response is", "the table response is" + result);
                }
            });
        } else
            ToastUtils.displayToast(Constants.no_internet_connection, context);

    }

    @Override
    public void subjectListResponse(SubjectListDetails subjectListDetails) {
        Log.v("the etails are ", "the details ..." + subjectListDetails);
    }

    @Override
    public void getTheSelectedPosition(int position) {
        popupToGetStudentsList(TeachersTimeTableActivity.this, position);
    }
    @Override
    public void createTimeTable(String result) {
        Log.v("the etails are ", "the details ..." + result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            String status = jsonObj.getString("status");
            String message = jsonObj.getString("message");
            if (status.equalsIgnoreCase("1"))
                ToastUtils.displayToast(message, getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void showTimeTableDetails(ShowTimetableDetails result) {
        ArrayList<TimeTable> tables = new ArrayList<>();
        TimeTable table = new TimeTable();
        List<org.com.classmate.model.showTimetable.TimeTable> timeList = result.getTimeTable();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 7; j++) {
                TimeTable item = new TimeTable();
                if (i == 0 && j == 0) {
                    item.setEnable("text");
                    item.setValue("UTC");
                } else if (i == 0 && j >= 1) {
                    item.setEnable("text");
                    String day = Constants.getDay(String.valueOf(j));
                    item.setValue(day);
                } else {
                    if (j == 0 && i >= 1) {
                        item.setEnable("text");
                        item.setValue(String.valueOf(time)+"0");
                    } else {
                        Log.v("the time table list is", "the list of time table is" + i + "the  day" + j);
                        for (int k = 0; k < timeList.size(); k++) {
                            Log.v("the time table list is", "the list of time table is...." + timeList.get(k).getPeriod() + "the  period" + timeList.get(k).getDay());
                            if (i == timeList.get(k).getPeriod() && j == timeList.get(k).getDay()) {
                                item.setEnable("text");
                                item.setValue(String.valueOf(timeList.get(k).getSubjectId()));
                            }
                        }
                    }

                }
                tables.add(item);
            }
            if (time == 12.30) {
                time = 14.00;
            } else
                time++;

            TimeTableAdapter adapter = new TimeTableAdapter(this, tables, TeachersTimeTableActivity.this);
            mImageGrid.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
