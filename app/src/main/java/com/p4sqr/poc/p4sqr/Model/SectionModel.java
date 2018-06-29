package com.p4sqr.poc.p4sqr.Model;

public class SectionModel {
    private String mName;
    private boolean isChecked;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public SectionModel(String mName, boolean isChecked) {
        this.mName = mName;
        this.isChecked = isChecked;

    }
}
