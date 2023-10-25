package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginCustDetailsModel {

    @SerializedName("CustID")
    @Expose
    private Integer custID;
    @SerializedName("FirmName")
    @Expose
    private String firmName;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("IsActive")
    @Expose
    private Boolean isActive;
    @SerializedName("IsBlocked")
    @Expose
    private Object isBlocked;
    @SerializedName("BlockedDate")
    @Expose
    private Object blockedDate;
    @SerializedName("tblQuestion")
    @Expose
    private TblQuestion tblQuestion;
    @SerializedName("Blockstatus")
    @Expose
    private Boolean blockstatus;
    @SerializedName("QuestionAnswered")
    @Expose
    private Boolean questionAnswered;


    @SerializedName("IsQuestionRequired")
    @Expose
    private Boolean IsQuestionRequired;



    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Object getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Object isBlocked) {
        this.isBlocked = isBlocked;
    }

    public Object getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(Object blockedDate) {
        this.blockedDate = blockedDate;
    }

    public TblQuestion getTblQuestion() {
        return tblQuestion;
    }

    public void setTblQuestion(TblQuestion tblQuestion) {
        this.tblQuestion = tblQuestion;
    }

    public Boolean getBlockstatus() {
        return blockstatus;
    }

    public void setBlockstatus(Boolean blockstatus) {
        this.blockstatus = blockstatus;
    }

    public Boolean getQuestionAnswered() {
        return questionAnswered;
    }


    public void setQuestionAnswered(Boolean questionAnswered) {
        this.questionAnswered = questionAnswered;
    }
    public Boolean getQuestionRequired() {
        return IsQuestionRequired;
    }

    public void setQuestionRequired(Boolean questionRequired) {
        IsQuestionRequired = questionRequired;
    }

    @Override
    public String toString(){
        return "LoginCustDetailsModel{"+
                "CustID = "+custID+
                ", FirmName ="+ firmName+'\''+
                ", MobileNumber + " +mobileNumber+'\''+
                ", IsActive = " +isActive+'\''+
                ", IsBlocked = " +isBlocked+'\''+
                ", BlockedDate = "+blockedDate+'\''+
                ", tblQuestion="+ tblQuestion+'\''+
                ", Blockstatus = "+blockstatus+'\''+
                ", IsQuestionRequired = "+IsQuestionRequired+'\''+
                ", QuestionAnswered = "+questionAnswered+'}';
    }
}
