package org.com.classmate.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.com.classmate.R;
import org.com.classmate.ui.activities.students.StudentsListActivity;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.customfonts.CustomTextViewBold;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

/**
 * Created by drkranga on 9/9/2017.
 */

public class TodaySubjectsAdapter extends RecyclerView.Adapter<TodaySubjectsAdapter.AttendanceViewHolder> {
    private static final String TAG = "TeacherAttendanceAdapter";
    private Context context;

    public TodaySubjectsAdapter(Context context) {
        this.context = context;
    }


    @Override
    public TodaySubjectsAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.snippet_subjects_row, parent, false);
        return new TodaySubjectsAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodaySubjectsAdapter.AttendanceViewHolder holder, final int position) {
        if (position == 1) {
            holder.tvSubjectsName.setText("Maths");
            holder.tvSubjectBranch.setText("Branch : EEE");
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        } else if (position == 2) {
            holder.tvSubjectsName.setText("Science");
            holder.tvSubjectBranch.setText("Branch : IT");
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.app_black_buttons_bg_color));
        } else if (position == 3) {
            holder.tvSubjectsName.setText("Computers");
            holder.tvSubjectBranch.setText("Branch : MECH");
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.blue));
        } else if (position == 4) {
            holder.tvSubjectsName.setText("C Language");
            holder.tvSubjectBranch.setText("Branch : CIVIL");
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
        }


    }

    @Override
    public int getItemCount() {
        return 5;
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold tvSubjectsName;
        CustomTextViewRegular tvSubjectBranch;
        RelativeLayout rlSubjectsRoot;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            tvSubjectsName = (CustomTextViewBold) itemView.findViewById(R.id.tv_subjectsName);
            tvSubjectBranch = (CustomTextViewRegular) itemView.findViewById(R.id.tv_subjectBranch);
            rlSubjectsRoot = (RelativeLayout) itemView.findViewById(R.id.rl_subject_root);
        }

    }
}
