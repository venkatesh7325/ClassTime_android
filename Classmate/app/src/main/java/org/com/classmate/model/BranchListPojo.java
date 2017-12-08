
package org.com.classmate.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BranchListPojo {

    @SerializedName("branch_list")
    @Expose
    private List<BranchList> branchList = null;

    public List<BranchList> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<BranchList> branchList) {
        this.branchList = branchList;
    }

}
