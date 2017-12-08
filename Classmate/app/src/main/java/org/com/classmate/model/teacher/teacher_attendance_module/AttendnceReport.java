
package org.com.classmate.model.teacher.teacher_attendance_module;


import android.os.Parcel;
import android.os.Parcelable;

public class AttendnceReport implements Parcelable {

    private String sName;
    private String sRollnumber;
    private String attendanceStatus;
    public AttendnceReport(String sRollnumber,String sName,String attendanceStatus){
        this.sName = sName;
        this.sRollnumber = sRollnumber;
        this.attendanceStatus = attendanceStatus;

    }

    protected AttendnceReport(Parcel in) {
        sName = in.readString();
        sRollnumber = in.readString();
        attendanceStatus = in.readString();
    }

    public static final Creator<AttendnceReport> CREATOR = new Creator<AttendnceReport>() {
        @Override
        public AttendnceReport createFromParcel(Parcel in) {
            return new AttendnceReport(in);
        }

        @Override
        public AttendnceReport[] newArray(int size) {
            return new AttendnceReport[size];
        }
    };

    public String getSName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName;
    }

    public String getSRollnumber() {
        return sRollnumber;
    }

    public void setSRollnumber(String sRollnumber) {
        this.sRollnumber = sRollnumber;
    }

    public String getAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(String attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(sName);
        dest.writeString(sRollnumber);
        dest.writeString(attendanceStatus);
    }
}
