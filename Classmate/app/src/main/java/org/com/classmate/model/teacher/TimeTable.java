package org.com.classmate.model.teacher;

/**
 * Created by Parnika on 16-07-2017.
 */

public class TimeTable {

    private String value ="";
    private String enable ="";
    private String enableBackground="";
    private String slected ="false";

    public String getSlected() {
        return slected;
    }

    public void setSlected(String slected) {
        this.slected = slected;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getEnableBackground() {
        return enableBackground;
    }

    public void setEnableBackground(String enableBackground) {
        this.enableBackground = enableBackground;
    }
}
