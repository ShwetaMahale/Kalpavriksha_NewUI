package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class AccountVerify {


    @SerializedName("CustID")
    private  int CustID;

    @SerializedName("DeviceID")
    private String DeviceID;

    @SerializedName("IsRegistered")
    private int IsRegistered;

    @SerializedName("MobileNumber")
    private String MobileNumber;

    @SerializedName("SMSOTP")
    private String SMSOTP;

    @SerializedName("OTPSent")
    private boolean OTPSent;

    @SerializedName("IsOTPVerified")
    private boolean IsOTPVerified;

    @SerializedName("QuestionAnswered")
    private boolean QuestionAnswered;

    @SerializedName("OTPStatus")
    private String OTPStatus;

    @SerializedName("AnswerForQuestion")
    private String AnswerForQuestion;

    @SerializedName("Question")
    private String Question;

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getIsRegistered() {
        return IsRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        IsRegistered = isRegistered;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
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

    public boolean isQuestionAnswered() {
        return QuestionAnswered;
    }

    public void setQuestionAnswered(boolean questionAnswered) {
        QuestionAnswered = questionAnswered;
    }

    public String getOTPStatus() {
        return OTPStatus;
    }

    public void setOTPStatus(String OTPStatus) {
        this.OTPStatus = OTPStatus;
    }

    public String getAnswerForQuestion() {
        return AnswerForQuestion;
    }

    public void setAnswerForQuestion(String answerForQuestion) {
        AnswerForQuestion = answerForQuestion;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public boolean isOTPSent() {
        return OTPSent;
    }

    public void setOTPSent(boolean OTPSent) {
        this.OTPSent = OTPSent;
    }

    @Override
    public String toString() {
        return "AccountVerify{" +
                "CustID=" + CustID +
                ", DeviceID='" + DeviceID + '\'' +
                ", IsRegistered=" + IsRegistered +
                ", MobileNumber='" + MobileNumber + '\'' +
                ", SMSOTP='" + SMSOTP + '\'' +
                ", IsOTPVerified=" + IsOTPVerified +
                ", QuestionAnswered=" + QuestionAnswered +
                ", OTPStatus='" + OTPStatus + '\'' +
                ", AnswerForQuestion='" + AnswerForQuestion + '\'' +
                ", Question='" + Question + '\'' +
                '}';
    }
}
