package org.com.classmate.model.admin;

import android.os.Parcel;
import android.os.Parcelable;

import org.com.classmate.model.common.Branches;

import java.util.ArrayList;

/**
 * Created by cts on 6/23/17.
 */

public class AdminDetails implements Parcelable{

    private String id ="";
    private String email = "";
    private String cname = "";
    private String ccode = "";
    private String pwd = "";
    private ArrayList<Branches> list_branches = new ArrayList<>();
    private ArrayList<String> list_contacts = new ArrayList<>();
    private String address = "";
    private String city = "";
    private String pincode = "";
    private String imagePath ="";

    public AdminDetails() {

    }

    public AdminDetails(Parcel in) {
        id = in.readString();
        email = in.readString();
        cname = in.readString();
        ccode = in.readString();
        pwd = in.readString();
        list_branches = in.createTypedArrayList(Branches.CREATOR);
        list_contacts = in.createStringArrayList();
        address = in.readString();
        city = in.readString();
        pincode = in.readString();
        imagePath = in.readString();
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(email);
        dest.writeString(cname);
        dest.writeString(ccode);
        dest.writeString(pwd);
        dest.writeTypedList(list_branches);
        dest.writeStringList(list_contacts);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(pincode);
        dest.writeString(imagePath);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdminDetails> CREATOR = new Creator<AdminDetails>() {
        @Override
        public AdminDetails createFromParcel(Parcel in) {
            return new AdminDetails(in);
        }

        @Override
        public AdminDetails[] newArray(int size) {
            return new AdminDetails[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public ArrayList<Branches> getList_branches() {
        return list_branches;
    }

    public void setList_branches(ArrayList<Branches> list_branches) {
        this.list_branches = list_branches;
    }

    public ArrayList<String> getList_contacts() {
        return list_contacts;
    }

    public void setList_contacts(ArrayList<String> list_contacts) {
        this.list_contacts = list_contacts;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
