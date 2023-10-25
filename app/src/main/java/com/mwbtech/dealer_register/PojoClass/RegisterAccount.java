package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class RegisterAccount {
    @SerializedName("FirmName")
    public String custName;
    @SerializedName("EmailID")
    public String emailID;
    @SerializedName("MobileNumber")
    public String mobileNumber;
    @SerializedName("Pincode")
    public String pincode;
    @SerializedName("Password")
    public String password;
    @SerializedName("ReferalCode")
    public String referalCode;
    @SerializedName("IsTAndCAgreed")
    public boolean isTAndCAgreed=true;
    @SerializedName("TAndCVersion")
    public String tAndCVersion;

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferalCode() {
        return referalCode;
    }

    public void setReferalCode(String referalCode) {
        this.referalCode = referalCode;
    }

    public boolean isTAndCAgreed() {
        return isTAndCAgreed;
    }

    public void setTAndCAgreed(boolean TAndCAgreed) {
        isTAndCAgreed = TAndCAgreed;
    }

    public String gettAndCVersion() {
        return tAndCVersion;
    }

    public void settAndCVersion(String tAndCVersion) {
        this.tAndCVersion = tAndCVersion;
    }

    public RegisterAccount(String custName, String emailID, String mobileNumber, String pincode, String password, String referalCode, boolean isTAndCAgreed, String tAndCVersion) {
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.pincode = pincode;
        this.password = password;
        this.referalCode = referalCode;
        this.isTAndCAgreed = isTAndCAgreed;
        this.tAndCVersion = tAndCVersion;
    }
}
