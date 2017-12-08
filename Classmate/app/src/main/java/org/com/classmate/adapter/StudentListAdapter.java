package org.com.classmate.adapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import org.com.classmate.R;
import org.com.classmate.model.students.StudentsList.StudentList;
import org.com.classmate.ui.activities.students.StudentsListActivity;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.customfonts.CustomTextViewBold;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

import java.util.List;

import static org.com.classmate.utils.Utility.doCall;
import static org.com.classmate.utils.Utility.senSms;

/**
 * Created by drkranga on 9/6/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.AttendanceViewHolder> {
    private static final String TAG = "StudentListAdapter";
    private Context context;
    private List<StudentList> studentLists;

    public StudentListAdapter(Context context, List<StudentList> studentLists) {
        this.context = context;
        this.studentLists = studentLists;
    }


    @Override
    public StudentListAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.snippet_students_list_row, parent, false);
        return new StudentListAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentListAdapter.AttendanceViewHolder holder, final int position) {

        holder.stdName.setText(studentLists.get(position).getName());
        holder.stdRollNumber.setText(studentLists.get(position).getRollNumber());
        holder.tvStudentMobileNumber.setText(studentLists.get(position).getMobile());

       /* holder.imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "CALL");
                String mobileNo = "7829648388";
                String uri = "tel:" + mobileNo.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                context.startActivity(intent);
            }
        });
        holder.imgSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "SMS");
                senSms(context, "7829648388");
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return studentLists.size();
    }

    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewMedium stdRollNumber;
        CustomTextViewRegular stdName;
        CustomTextViewMedium tvStudentMobileNumber;
        ImageView imgCall;
        ImageView imgSms;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            stdRollNumber = (CustomTextViewMedium) itemView.findViewById(R.id.tv_student_rollnum);
            stdName = (CustomTextViewRegular) itemView.findViewById(R.id.tv_student_name);
            tvStudentMobileNumber = (CustomTextViewMedium) itemView.findViewById(R.id.tv_mobile_number);
            imgCall = (ImageView) itemView.findViewById(R.id.img_call_student);
            imgSms = (ImageView) itemView.findViewById(R.id.img_sms_student);


        }

    }
}