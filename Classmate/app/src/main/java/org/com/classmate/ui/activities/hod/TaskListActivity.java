package org.com.classmate.ui.activities.hod;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.adapter.TaskListAdapter;
import org.com.classmate.model.TaskModule.taskList.TaskList;
import org.com.classmate.model.TaskModule.taskList.TaskListModel;
import org.com.classmate.ui.activities.teacher.TeachersTaskAssignActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomTextViewBold;

import java.util.HashMap;
import java.util.List;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "tasklist";
    Spinner spYear;
    Spinner spSemister;
    EditText edtTaskSubject;
    CustomTextViewBold tvNoTasks;
    RecyclerView rcvSubjectsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        initToolBar();
        rcvSubjectsList = (RecyclerView) findViewById(R.id.rcv_subjects);
        tvNoTasks = (CustomTextViewBold) findViewById(R.id.tv_no_result);
        if (Utility.isConnectingToInternet(this))
            callApiGetTaskList(rcvSubjectsList);
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);
        
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskListActivity.this, TeachersTaskAssignActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Utility.isConnectingToInternet(this))
            callApiGetTaskList(rcvSubjectsList);
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);
    }

    private void setAdapter(RecyclerView rcvSubjectsList, List<TaskList> lists) {
        TaskListAdapter subjectsAdapter = new TaskListAdapter(TaskListActivity.this, lists);
        rcvSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        rcvSubjectsList.setAdapter(subjectsAdapter);
    }

    void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Task list");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    void callApiGetTaskList(final RecyclerView rcvSubjectsList) {
        final HashMap<String, String> jsonObject = new HashMap<>();
        try {
            //  jsonObject.put("branch_id", "10");
            // jsonObject.put("institution_id", String.valueOf(Utility.getUserID(TaskListActivity.this)));
            //jsonObject.put("year", Constants.getYearId(spYear.getSelectedItem().toString()));
            //jsonObject.put("semester", Constants.getYearId(spSemister.getSelectedItem().toString()));
            jsonObject.put("teacher_id", String.valueOf(Utility.getUserID(TaskListActivity.this)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "After hash map");
        Log.d(TAG, "Hash Map aprams--" + jsonObject);
        new APIRequest(this).postStringRequest(ApiConstants.TASK_LIST_TEACHER, jsonObject, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "TASK Activity--" + response);
                Gson gson = new Gson();
                TaskListModel taskListModel = gson.fromJson(response, TaskListModel.class);
                if (taskListModel.getStatus().equals("1")) {
                    // ToastUtils.displayToast(taskListModel.getMessage(), TaskListActivity.this);
                    setAdapter(rcvSubjectsList, taskListModel.getTaskList());

                } else {
                    //  ToastUtils.displayToast(taskListModel.getMessage(), TaskListActivity.this);
                    tvNoTasks.setVisibility(View.VISIBLE);
                    tvNoTasks.setText(taskListModel.getMessage());
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, TaskListActivity.this);
                Log.d(TAG, "TASK Error message--" + message + "--jon respons--" + jsonObject);

            }
        });


    }


}
