package org.com.classmate.ui.activities.students;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.RequestCallBack;
import org.com.classmate.R;
import org.com.classmate.model.StudentDetails.GetStudentDetails;
import org.com.classmate.model.hod.TeacherData.GetTeacherDetailsModel;
import org.com.classmate.ui.activities.hod.TaskListActivity;
import org.com.classmate.ui.activities.teacher.TeachersDashboardActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;

import java.util.HashMap;

public class StudentDashBoardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = StudentDashBoardActivity.class.getSimpleName();
    TextView tvName, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);

        ImageView imfProfile = (ImageView) view.findViewById(R.id.nav_header_profileImage);
        tvName = (TextView) view.findViewById(R.id.nav_header_username);
        tvEmail = (TextView) view.findViewById(R.id.nav_header_email);


        if (Utility.isConnectingToInternet(this))
            callApiToGetUserDetails(this);
        else
            ToastUtils.displayToast(Constants.no_internet_connection, this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Constants.popupToLogOut(StudentDashBoardActivity.this);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_classes) {
            // Handle the camera action
            startActivity(new Intent(this, StudentDayActivity.class));
        } else if (id == R.id.nav_attendance) {
            ToastUtils.displayToast("In progress", this);
        } else if (id == R.id.nav_score) {
            // ToastUtils.displayToast("In progress", this);
        } else if (id == R.id.nav_assignments) {
            startActivity(new Intent(StudentDashBoardActivity.this, TaskListActivity.class));
        } else if (id == R.id.nav_event) {
            startActivity(new Intent(StudentDashBoardActivity.this, StudentEventsActivity.class));
        } else if (id == R.id.nav_logout) {
            Constants.popupToLogOut(StudentDashBoardActivity.this);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void callApiToGetUserDetails(final Context context) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        try {
            hashMap.put("id", String.valueOf(Utility.getUserID(context)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Teach User--" + hashMap);
        new APIRequest(context).postStringRequest(ApiConstants.GET_USER_DETAILS, hashMap, new RequestCallBack() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Dash Teach Activity--" + response);
                Gson gson = new Gson();
                GetStudentDetails successResponsePojo = gson.fromJson(response, GetStudentDetails.class);
                if (successResponsePojo.getMessage().equalsIgnoreCase("success")) {
                    tvName.setText(successResponsePojo.getUserDetails().getName());
                    tvEmail.setText(successResponsePojo.getUserDetails().getEmail());
                    Utility.saveUserID(StudentDashBoardActivity.this, successResponsePojo.getUserDetails().getId()); // saving user ID into pref
                    Utility.saveInstitutionID(StudentDashBoardActivity.this, String.valueOf(successResponsePojo.getUserDetails().getUniversityId()));
                } else {
                    ToastUtils.displayToast(successResponsePojo.getMessage(), context);
                }
            }

            @Override
            public void onFailed(String message) {
                ToastUtils.displayToast(Constants.something_went_wrong, context);
                Log.d(TAG, "Login Error message--" + message + "--jon respons--" + hashMap);

            }
        });
    }
}
