
package org.com.classmate.model.subjectList;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectListDetails implements Parcelable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("subject_list")
    @Expose
    private List<SubjectList> subjectList = new ArrayList<>();

    public SubjectListDetails() {

    }

    protected SubjectListDetails(Parcel in) {
        status = in.readString();
        message = in.readString();
        subjectList = in.createTypedArrayList(SubjectList.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeString(message);
        dest.writeTypedList(subjectList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubjectListDetails> CREATOR = new Creator<SubjectListDetails>() {
        @Override
        public SubjectListDetails createFromParcel(Parcel in) {
            return new SubjectListDetails(in);
        }

        @Override
        public SubjectListDetails[] newArray(int size) {
            return new SubjectListDetails[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SubjectList> getSubjectList() {
        return subjectList;
    }

    public void setSubjectList(List<SubjectList> subjectList) {
        this.subjectList = subjectList;
    }

}
