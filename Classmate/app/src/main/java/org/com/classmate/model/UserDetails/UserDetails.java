
package org.com.classmate.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetails {

    /*{"message":"success","user_details":{"id":3,"name":"Pragathi college of engineering","code":"PCEEAD3","college_code":"PCEE","university_id":3,"email":"venky@gmail.com","mobile":"7829648388","address":"Bangalore","city":"Bangalore","pincode":560043,"user_id":3,"status":1,"created_at":"2017-09-30 07:33:05","updated_at":"2017-09-30 07:33:05","deleted_at":null}}*/

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("gender_id")
    @Expose
    private Integer genderId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("institution_id")
    @Expose
    private Integer institutionId;
    @SerializedName("branch_id")
    @Expose
    private Integer branchId;
    @SerializedName("qualification")
    @Expose
    private String qualification;
    @SerializedName("mobile")
    @Expose
    private Integer mobile;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGenderId() {
        return genderId;
    }

    public void setGenderId(Integer genderId) {
        this.genderId = genderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Integer institutionId) {
        this.institutionId = institutionId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getMobile() {
        return mobile;
    }

    public void setMobile(Integer mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

}
