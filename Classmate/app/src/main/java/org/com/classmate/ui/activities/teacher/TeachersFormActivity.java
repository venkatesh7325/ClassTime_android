package org.com.classmate.ui.activities.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.com.classmate.R;
import org.com.classmate.utils.DynamicCustomButtons;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cts on 6/29/17.
 */

public class TeachersFormActivity extends AppCompatActivity{

    /*@Bind(R.id.edt_cvcode)
    EditText edtHodCode;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
     //   edtHodCode.setHint("Please Enter Hod Verification Code");
        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TeachersFormActivity.this, TeachersDashboardActivity.class));
            }
        });

    }


}
