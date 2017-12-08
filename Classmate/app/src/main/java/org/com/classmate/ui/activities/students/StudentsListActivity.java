package org.com.classmate.ui.activities.students;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import org.com.classmate.R;
import org.com.classmate.adapter.DepartMentListAdapter;
import org.com.classmate.adapter.StudentListAdapter;
import org.com.classmate.model.students.StudentsList.GetStudentsListPojo;
import org.com.classmate.model.students.StudentsList.StudentList;

import java.util.List;

public class StudentsListActivity extends AppCompatActivity {
    RecyclerView rcvStdList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);
        initToolBar();
        rcvStdList = (RecyclerView) findViewById(R.id.rcv_student_list);
        getParecDataFromBundle(); //

    }

    void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Attendance");
    }

    void getParecDataFromBundle() {
        try {
            if (getIntent() != null && getIntent().getExtras() != null) {
                List<StudentList> getStudentsListPojo = getIntent().getExtras().getParcelableArrayList("student_list");
                if (getStudentsListPojo != null && getStudentsListPojo.size() > 0)
                    setAdapter(getStudentsListPojo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setAdapter(List<StudentList> studentLists) {
        StudentListAdapter departMentListAdapter = new StudentListAdapter(this, studentLists);
        rcvStdList.setLayoutManager(new LinearLayoutManager(this));
        rcvStdList.setAdapter(departMentListAdapter);
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
}



