
package org.com.classmate.model.GetClz.GetDetailsFromClzRegId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCollegeIDPojo {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("college_details")
    @Expose
    private CollegeDetails collegeDetails;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CollegeDetails getCollegeDetails() {
        return collegeDetails;
    }

    public void setCollegeDetails(CollegeDetails collegeDetails) {
        this.collegeDetails = collegeDetails;
    }

}
