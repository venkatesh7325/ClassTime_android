package org.com.classmate.ui.activities.students;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.com.classmate.R;
import org.com.classmate.utils.ToastUtils;

public class StudentDayActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout llClasses;
    LinearLayout llAssignments;
    LinearLayout llPerformance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studnet_day);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Classes");
        llClasses = (LinearLayout) findViewById(R.id.ll_classes);
        llAssignments = (LinearLayout) findViewById(R.id.ll_assignments);
        llPerformance = (LinearLayout) findViewById(R.id.ll_performance);
        llClasses.setOnClickListener(this);
        llAssignments.setOnClickListener(this);
        llPerformance.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_classes:
                startActivity(new Intent(this, StudentClassesActivity.class));
                break;
            case R.id.ll_assignments:
                startActivity(new Intent(this, StudentClassesActivity.class));
                break;
            case R.id.ll_performance:
                ToastUtils.displayToast("In progress", this);
                break;
            default:
                break;
        }
    }
}
