
package org.com.classmate.model.teacher.teacher_attendance_module;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class TeacherAttendanceModel implements Parcelable {

    private String teacherCode;
    private String dept;
    private String year;
    private String sem;
    private String attendanceDate;
    private List<AttendnceReport> attendnceReport = new ArrayList<>();
    public TeacherAttendanceModel(){

    }

    public TeacherAttendanceModel(Parcel in) {
        teacherCode = in.readString();
        dept = in.readString();
        year = in.readString();
        sem = in.readString();
        attendanceDate = in.readString();
        attendnceReport = in.createTypedArrayList(AttendnceReport.CREATOR);
    }

    public static final Creator<TeacherAttendanceModel> CREATOR = new Creator<TeacherAttendanceModel>() {
        @Override
        public TeacherAttendanceModel createFromParcel(Parcel in) {
            return new TeacherAttendanceModel(in);
        }

        @Override
        public TeacherAttendanceModel[] newArray(int size) {
            return new TeacherAttendanceModel[size];
        }
    };

    public String getTeacherCode() {
        return teacherCode;
    }

    public void setTeacherCode(String teacherCode) {
        this.teacherCode = teacherCode;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public List<AttendnceReport> getAttendnceReport() {
        return attendnceReport;
    }

    public void setAttendnceReport(List<AttendnceReport> attendnceReport) {
        this.attendnceReport = attendnceReport;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(teacherCode);
        dest.writeString(dept);
        dest.writeString(year);
        dest.writeString(sem);
        dest.writeString(attendanceDate);
        dest.writeTypedList(attendnceReport);
    }
}
