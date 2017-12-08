
package org.com.classmate.model.teacher.teacher_attendance_module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttendanceReport {

    @SerializedName("student_id")
    @Expose
    private String studentId;
    @SerializedName("attendance_status")
    @Expose
    private String attendanceStatus;
    public AttendanceReport(String studentId,String attendanceStatus){
        this.studentId = studentId;
        this.attendanceStatus = attendanceStatus;

    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

}
