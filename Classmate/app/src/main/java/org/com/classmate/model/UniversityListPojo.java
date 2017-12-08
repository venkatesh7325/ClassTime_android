
package org.com.classmate.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UniversityListPojo {

    @SerializedName("university_list")
    @Expose
    private List<UniversityList> universityList = null;

    public List<UniversityList> getUniversityList() {
        return universityList;
    }

    public void setUniversityList(List<UniversityList> universityList) {
        this.universityList = universityList;
    }

}
