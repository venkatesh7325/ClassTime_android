package org.com.classmate.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.mikhaellopez.circularimageview.CircularImageView;

import org.com.classmate.R;
import org.com.classmate.model.students.StudentsList.StudentList;
import org.com.classmate.model.teacher.teacher_attendance_module.AttendanceReport;
import org.com.classmate.model.teacher.teacher_attendance_module.AttendnceReport;
import org.com.classmate.model.teacher.teacher_attendance_module.TeacherAttendanceModel;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.customfonts.CustomEditTextMedium;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by mahiti on 20/10/16.
 */
public class TeacherAttendanceAdapter extends RecyclerView.Adapter<TeacherAttendanceAdapter.AttendanceViewHolder> {
    private static final String TAG = "TeacherAtnceAdapter";
    private Context context;
    // List<TeacherAttendanceModel> teacherAttendanceModelsList = new ArrayList<>();
    //  List<AttendnceReport> teacherAttendanceModelsList;
    List<StudentList> getStudentsListPojo;
    //  List<AttendnceReport> generateAttendanceList = new ArrayList<>();
    HashMap<String, AttendanceReport> mapAttendance = new HashMap<>();

    public TeacherAttendanceAdapter(Context context, List<StudentList> getStudentsListPojo) {
        this.context = context;
        this.getStudentsListPojo = getStudentsListPojo;
    }


    @Override
    public AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.snippet_attendance_row, parent, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AttendanceViewHolder holder, final int position) {
        holder.stdName.setText(getStudentsListPojo.get(position).getName());
        holder.stdRollNumber.setText(getStudentsListPojo.get(position).getRollNumber());
        holder.rgStudentAttendanceStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId != -1) {
                    RadioButton checked_rb = (RadioButton) group.findViewById(checkedId);
                    Log.d(TAG, "Check item by Name--" + getStudentsListPojo.get(position).getName() + "--Checked item--" + checked_rb.getText().toString());
                    mapAttendance.put(getStudentsListPojo.get(position).getRollNumber(), new AttendanceReport(getStudentsListPojo.get(position).getRollNumber(), checked_rb.getText().toString()));
                    // generateAttendanceList.add(position, new AttendnceReport(teacherAttendanceModelsList.get(position).getSRollnumber(), teacherAttendanceModelsList.get(position).getSName(), checked_rb.getText().toString()));
                    Log.d(TAG, "Checked radio button mapAttendance size--" + mapAttendance.size());
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return getStudentsListPojo.size();
    }

    public HashMap<String, AttendanceReport> getSlectedAttendanceList() {
        return mapAttendance;
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewMedium stdRollNumber;
        CustomTextViewRegular stdName;
        CircularImageView circularImageView;
        RadioGroup rgStudentAttendanceStatus;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            rgStudentAttendanceStatus = (RadioGroup) itemView.findViewById(R.id.rg_student_attendance);
            stdRollNumber = (CustomTextViewMedium) itemView.findViewById(R.id.tv_student_rollnum);
            stdName = (CustomTextViewRegular) itemView.findViewById(R.id.tv_student_name);
        }

    }
}