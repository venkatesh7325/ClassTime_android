
package org.com.classmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UniversityList {

    @SerializedName("university_id")
    @Expose
    private Integer universityId;
    @SerializedName("university_name")
    @Expose
    private String universityName;

    public Integer getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Integer universityId) {
        this.universityId = universityId;
    }

    public String getUniversityName() {
        return universityName;
    }

    public void setUniversityName(String universityName) {
        this.universityName = universityName;
    }
    public String toString() {
        return universityName;
    }

}
