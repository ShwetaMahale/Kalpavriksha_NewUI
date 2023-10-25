package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class District {

    @SerializedName("ID")
    private int DistrictID;

    @SerializedName("DistrictName")
    private String DistrictName;

    private boolean isChecked=false;

    public District(int districtID, String districtName, boolean isChecked) {
        DistrictID = districtID;
        DistrictName = districtName;
        this.isChecked = isChecked;
    }

    public District(int districtID) {
        DistrictID = districtID;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    @Override
    public String toString() {
        return DistrictName;
    }
}
