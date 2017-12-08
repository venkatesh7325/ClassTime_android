
package org.com.classmate.model.GetClz;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCollegeResponsePojo {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

}
