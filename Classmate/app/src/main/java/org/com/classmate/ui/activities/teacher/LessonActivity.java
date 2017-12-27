package org.com.classmate.ui.activities.teacher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.R;
import org.com.classmate.adapter.TodaySubjectsAdapter;
import org.com.classmate.interfaces.ShowTimetable;
import org.com.classmate.model.showTimetable.ShowTimetableDetails;
import org.com.classmate.model.showTimetable.TimeTable;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.ToastUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LessonActivity extends AppCompatActivity implements ShowTimetable {

    RecyclerView rcvSubjectsList;
    TodaySubjectsAdapter subjectsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);
        initToolBar();
        APIRequest.showTimeTable(LessonActivity.this, "2", "1", LessonActivity.this);
        rcvSubjectsList = (RecyclerView) findViewById(R.id.rcv_subjects);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Today Subjects");
    }

    @Override
    public void showTimeTableDetails(ShowTimetableDetails result) {
        Logger.logV("the result is", "the result is" + result);
        List<TimeTable> timeTable = result.getTimeTable();
        subjectsAdapter = new TodaySubjectsAdapter(LessonActivity.this, timeTable);
        rcvSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        rcvSubjectsList.setAdapter(subjectsAdapter);

    }
}
