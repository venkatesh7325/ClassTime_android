
package org.com.classmate.model.teacher.teacher_attendance_module;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubmitAttendancePojo {

    @SerializedName("teacher_id")
    @Expose
    private String teacherId;
    @SerializedName("branch_id")
    @Expose
    private String branchId;
    @SerializedName("year")
    @Expose
    private String year;
    @SerializedName("semester")
    @Expose
    private String semester;
    @SerializedName("attendance_date")
    @Expose
    private String attendanceDate;
    @SerializedName("attendance_report")
    @Expose
    private List<AttendanceReport> attendanceReport = new ArrayList<>();

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public List<AttendanceReport> getAttendanceReport() {
        return attendanceReport;
    }

    public void setAttendanceReport(List<AttendanceReport> attendanceReport) {
        this.attendanceReport = attendanceReport;
    }

}
