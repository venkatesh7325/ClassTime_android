package org.com.classmate.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cts on 6/23/17.
 */

public class Branches implements Parcelable{
    String id = "";
    String name = "";
    public Branches(){

    }

    protected Branches(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Branches> CREATOR = new Creator<Branches>() {
        @Override
        public Branches createFromParcel(Parcel in) {
            return new Branches(in);
        }

        @Override
        public Branches[] newArray(int size) {
            return new Branches[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
