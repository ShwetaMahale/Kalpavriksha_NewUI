package com.mwbtech.dealer_register.Utils;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordRequest {

    private String password;
    private String cPasword;
    @SerializedName("MobileNo")
    private String mobile;
    private String NewPassowrd;

    public String getNewPassowrd() {
        return NewPassowrd;
    }

    public void setNewPassowrd(String newPassowrd) {
        NewPassowrd = newPassowrd;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getcPasword() {
        return cPasword;
    }

    public void setcPasword(String cPasword) {
        this.cPasword = cPasword;
    }
}
