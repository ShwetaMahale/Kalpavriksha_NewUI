package com.mwbtech.dealer_register.PojoClass;


public class OTPValidation {


     private Integer IsRegistered;
    private String UserType;
    private String Password;
    private String SMSOTP;
    private  boolean IsOTPVerified;
    private  String OTPStatus = "";


    public Integer getIsRegistered() {
        return IsRegistered;
    }

    public void setIsRegistered(Integer isRegistered) {
        IsRegistered = isRegistered;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getSMSOTP() {
        return SMSOTP;
    }

    public void setSMSOTP(String SMSOTP) {
        this.SMSOTP = SMSOTP;
    }

    public boolean isOTPVerified() {
        return IsOTPVerified;
    }

    public void setOTPVerified(boolean OTPVerified) {
        IsOTPVerified = OTPVerified;
    }

    public String getOTPStatus() {
        return OTPStatus;
    }

    public void setOTPStatus(String OTPStatus) {
        this.OTPStatus = OTPStatus;
    }
}
