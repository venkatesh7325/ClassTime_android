
package org.com.classmate.model.GetClz.GetDetailsFromClzRegId;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CollegeDetails {

    @SerializedName("university_id")
    @Expose
    private Integer universityId;
    @SerializedName("university_name")
    @Expose
    private String universityName;
    @SerializedName("institution_id")
    @Expose
    private Integer institutionId;
    @SerializedName("institution_name")
    @Expose
    private String institutionName;
    @SerializedName("branch_id")
    @Expose
    private Integer branchId;
    @SerializedName("branch_name")
    @Expose
    private String branchName;

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

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
