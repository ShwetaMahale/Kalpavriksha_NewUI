package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class SocialLoginRequest {

    private String SocialMediaToken = "";
    private String MobileNumber;
    @SerializedName("EmailID")
    private String Email;
    @SerializedName("CustName")
    private String name;

    public String getSocialMediaToken() {
        return SocialMediaToken;
    }

    public void setSocialMediaToken(String socialMediaToken) {
        SocialMediaToken = socialMediaToken;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.MobileNumber = mobileNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

