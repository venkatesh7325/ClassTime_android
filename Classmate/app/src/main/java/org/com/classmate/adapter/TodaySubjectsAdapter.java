package org.com.classmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.com.classmate.R;
import org.com.classmate.model.showTimetable.TimeTable;
import org.com.classmate.utils.customfonts.CustomTextViewBold;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

import java.util.List;

/**
 * Created by drkranga on 9/9/2017.
 */

public class TodaySubjectsAdapter extends RecyclerView.Adapter<TodaySubjectsAdapter.AttendanceViewHolder> {
    private static final String TAG = "TeacherAttendanceAdapter";
    private Context context;
    List<TimeTable> table;

    public TodaySubjectsAdapter(Context context, List<TimeTable> timeTable) {
        this.context = context;
        table=timeTable;
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
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.tvYear.setText(""+table.get(position).getYear());
        } else if (position == 2) {
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.app_black_buttons_bg_color));
            holder.tvYear.setText(""+table.get(position).getYear());
        } else if (position == 3) {
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.blue));
            holder.tvYear.setText(""+table.get(position).getYear());
        } else if (position == 4) {
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.tvYear.setText(""+table.get(position).getYear());
        }
        else if (position == 5) {
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.app_green_color));
            holder.tvYear.setText(""+table.get(position).getYear());
        }
        else  {
            holder.tvSubjectsName.setText(""+table.get(position).getSubject_name());
            holder.tvSubjectBranch.setText(""+table.get(position).getBranchId());
            holder.rlSubjectsRoot.setBackgroundColor(context.getResources().getColor(R.color.app_black_buttons_bg_color));
            holder.tvYear.setText(""+table.get(position).getYear());
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
        CustomTextViewRegular tvYear;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            tvSubjectsName = (CustomTextViewBold) itemView.findViewById(R.id.tv_subjectsName);
            tvSubjectBranch = (CustomTextViewRegular) itemView.findViewById(R.id.tv_subjectBranch);
            rlSubjectsRoot = (RelativeLayout) itemView.findViewById(R.id.rl_subject_root);
            tvYear = (CustomTextViewRegular) itemView.findViewById(R.id.tv_year);
        }

    }
}
