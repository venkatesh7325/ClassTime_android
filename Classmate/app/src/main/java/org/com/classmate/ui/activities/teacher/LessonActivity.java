package org.com.classmate.ui.activities.teacher;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.com.classmate.R;
import org.com.classmate.adapter.TodaySubjectsAdapter;

public class LessonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        initToolBar();
        RecyclerView rcvSubjectsList = (RecyclerView) findViewById(R.id.rcv_subjects);
        TodaySubjectsAdapter subjectsAdapter = new TodaySubjectsAdapter(LessonActivity.this);
        rcvSubjectsList.setLayoutManager(new LinearLayoutManager(this));
        rcvSubjectsList.setAdapter(subjectsAdapter);
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
}
