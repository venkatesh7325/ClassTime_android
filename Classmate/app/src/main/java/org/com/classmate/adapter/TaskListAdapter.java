package org.com.classmate.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.com.classmate.R;
import org.com.classmate.model.TaskModule.taskList.TaskList;
import org.com.classmate.utils.customfonts.CustomTextViewMedium;
import org.com.classmate.utils.customfonts.CustomTextViewRegular;

import java.util.List;

/**
 * Created by venkatesh on 11/28/2017.
 */

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.AttendanceViewHolder> {
    private static final String TAG = "TaskListAdapter";
    private Context context;
    List<TaskList> taskLists;

    public TaskListAdapter(Context context, List<TaskList> taskLists) {
        this.context = context;
        this.taskLists = taskLists;
    }


    @Override
    public TaskListAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.snippet_task_list_row, parent, false);
        return new TaskListAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskListAdapter.AttendanceViewHolder holder, final int position) {
        try {
            TaskList taskList = taskLists.get(position);

            if (taskList.getTitle() != null)
                holder.taskName.setText(taskList.getTitle());
            if (taskList.getDescription() != null)
                holder.taskDesc.setText(taskList.getDescription());

            if (taskList.getStartDate() != null)
                holder.taskStartDate.setText(taskList.getStartDate());
            if (taskList.getEndDate() != null)
                holder.taskEndDate.setText(taskList.getEndDate());
            if (taskList.getSubject_name() != null)
                holder.subjectName.setText(taskList.getSubject_name());


      /*  holder.taskStatus.setText(taskList.get());
        holder.taskComment.setText(taskList.get());*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return taskLists.size();
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewMedium subjectName;
        CustomTextViewRegular taskName;
        CustomTextViewRegular taskDesc;
        CustomTextViewRegular taskStartDate;
        CustomTextViewRegular taskEndDate;
        CustomTextViewRegular taskStatus;
        CustomTextViewRegular taskComment;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            taskName = (CustomTextViewRegular) itemView.findViewById(R.id.taskName);
            taskDesc = (CustomTextViewRegular) itemView.findViewById(R.id.taskDesc);

            taskStartDate = (CustomTextViewRegular) itemView.findViewById(R.id.taskStartDate);
            taskEndDate = (CustomTextViewRegular) itemView.findViewById(R.id.taskEndDate);
            subjectName = (CustomTextViewMedium) itemView.findViewById(R.id.subjectName);
        }

    }
}