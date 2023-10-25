package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CustomerInfo  implements Serializable {

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("FirmName")
    private String FirmName;

    @SerializedName("CityID")
    private int CityID;

    @SerializedName("StateID")
    private int StateID;

    @SerializedName("MobileNumber")
    private String MobileNumber;

    @SerializedName("EmailID")
    private String EmailID;


    public CustomerInfo(int custID, String firmName, int cityID, int stateID, String stateName, String cityName,String mobileNumber,String emailID) {
        CustID = custID;
        FirmName = firmName;
        CityID = cityID;
        StateID = stateID;
        StateName = stateName;
        CityName = cityName;
        MobileNumber=mobileNumber;
        EmailID=emailID;
    }

    @SerializedName("StateName")
    private String StateName;

    @SerializedName("CityName")
    private String CityName;

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

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
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
}
