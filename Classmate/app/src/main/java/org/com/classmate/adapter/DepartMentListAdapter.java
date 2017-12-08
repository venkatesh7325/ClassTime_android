package org.com.classmate.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import org.com.classmate.APIS.APIRequest;
import org.com.classmate.APIS.StudentListResponse;
import org.com.classmate.R;
import org.com.classmate.model.BranchList;
import org.com.classmate.model.common.Branches;
import org.com.classmate.model.students.StudentsList.GetStudentsListPojo;
import org.com.classmate.ui.activities.students.StudentsListActivity;
import org.com.classmate.utils.ApiConstants;
import org.com.classmate.utils.Constants;
import org.com.classmate.utils.Logger;
import org.com.classmate.utils.ToastUtils;
import org.com.classmate.utils.Utility;
import org.com.classmate.utils.customfonts.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drkranga on 9/5/2017.
 */

public class DepartMentListAdapter extends RecyclerView.Adapter<DepartMentListAdapter.AttendanceViewHolder> {
    private static final String TAG = "TeacherAttendanceAdapter";
    private Activity context;
    private List<BranchList> branchesList;
    String branchId = "";

    public DepartMentListAdapter(Activity context, List<BranchList> branchesList) {
        this.context = context;
        this.branchesList = branchesList;
    }


    @Override
    public DepartMentListAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.snippet_dept_list_row, parent, false);
        return new DepartMentListAdapter.AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartMentListAdapter.AttendanceViewHolder holder, final int position) {
        holder.tvDeptName.setText(branchesList.get(position).getBranchName());
        holder.tvDeptName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                branchId = String.valueOf(branchesList.get(position).getBranchId());
                popupToShowDiscounts();
            }
        });

    }

    @Override
    public int getItemCount() {
        return branchesList.size();
    }

    private void popupToShowDiscounts() {
        try {
            @SuppressLint("RestrictedApi") final Dialog dialog = new Dialog(new ContextThemeWrapper(context, R.style.Animation));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.discount_popup);
            dialog.setTitle("Select Year and Semester");
            if (dialog.getWindow() != null)
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.show();
            ImageView imageClose = (ImageView) dialog.findViewById(R.id.close);
            imageClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
            final Spinner spYear = (Spinner) dialog.findViewById(R.id.sp_choose_years);
            final Spinner spSem = (Spinner) dialog.findViewById(R.id.sp_choose_semi);
            Button btnSubmit = (Button) dialog.findViewById(R.id.btn_submit);
            btnSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (context.getResources().getString(R.string.Spiner_year_prompt).equalsIgnoreCase(spYear.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select year", context);
                        return;
                    }
                    if (context.getResources().getString(R.string.Spiner_semi_prompt).equalsIgnoreCase(spSem.getSelectedItem().toString())) {
                        ToastUtils.displayToast("Please select semester", context);
                        return;
                    }

                    if (Utility.isConnectingToInternet(context))
                        APIRequest.callApiToGetStudentList(context, Constants.getYearId(spYear.getSelectedItem().toString()), Constants.getYearId(spSem.getSelectedItem().toString()), branchId,"3", new StudentListResponse() {
                            @Override
                            public void stdList(GetStudentsListPojo getStudentsListPojo) {
                                Logger.logD(TAG,"List-size--"+getStudentsListPojo.getStudentList().size());
                                Bundle bundle = new Bundle();
                              //  bundle.putParcelableArrayList("student_list", getStudentsListPojo);
                                Intent i = new Intent(context, StudentsListActivity.class);
                                i.putParcelableArrayListExtra("student_list",(ArrayList<? extends Parcelable>) getStudentsListPojo.getStudentList());
                                context.startActivity(i);
                            }
                        });
                    else
                        ToastUtils.displayToast(Constants.no_internet_connection, context);

                    if (dialog.isShowing())
                        dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class AttendanceViewHolder extends RecyclerView.ViewHolder {
        CustomTextViewBold tvDeptName;

        public AttendanceViewHolder(View itemView) {
            super(itemView);
            tvDeptName = (CustomTextViewBold) itemView.findViewById(R.id.tv_dept_name);
        }

    }


}