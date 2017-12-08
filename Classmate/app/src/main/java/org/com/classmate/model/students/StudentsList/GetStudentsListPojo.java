
package org.com.classmate.model.students.StudentsList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetStudentsListPojo implements Parcelable{

    @SerializedName("student_list")
    @Expose
    private List<StudentList> studentList = new ArrayList<>();

    protected GetStudentsListPojo(Parcel in) {
    }

    public static final Creator<GetStudentsListPojo> CREATOR = new Creator<GetStudentsListPojo>() {
        @Override
        public GetStudentsListPojo createFromParcel(Parcel in) {
            return new GetStudentsListPojo(in);
        }

        @Override
        public GetStudentsListPojo[] newArray(int size) {
            return new GetStudentsListPojo[size];
        }
    };

    public List<StudentList> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<StudentList> studentList) {
        this.studentList = studentList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
