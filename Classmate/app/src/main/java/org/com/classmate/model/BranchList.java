
package org.com.classmate.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchList {

    @SerializedName("branch_id")
    @Expose
    private Integer branchId;
    @SerializedName("branch_name")
    @Expose
    private String branchName;

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
    public String toString()
    {
        return branchName;
    }

}
