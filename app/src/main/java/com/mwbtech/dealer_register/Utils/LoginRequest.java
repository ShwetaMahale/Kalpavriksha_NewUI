package com.mwbtech.dealer_register.Utils;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {

    @SerializedName("MobileNo")
    private String MobileNo;
    @SerializedName("Password")
    private String Password;

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
