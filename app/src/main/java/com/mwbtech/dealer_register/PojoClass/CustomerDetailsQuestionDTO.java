package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerDetailsQuestionDTO implements Serializable {
    @SerializedName("ID")
    private int CustID;

    @SerializedName("FirmName")
    private String FirmName;

    @SerializedName("MobileNumber")
    private String MobileNumber;

    @SerializedName("EmailID")
    private String EmailID;

    @SerializedName("VillageLocalityName")
    private String CityName;
    @SerializedName("UserImage")
    private String UserImage;


    public CustomerDetailsQuestionDTO(int custID, String firmName, String mobileNumber, String emailID, String cityName) {
        CustID = custID;
        FirmName = firmName;
        MobileNumber = mobileNumber;
        EmailID = emailID;
        CityName = cityName;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String emailID) {
        EmailID = emailID;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
    }
}
