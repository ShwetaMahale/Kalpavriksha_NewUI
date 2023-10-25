package com.mwbtech.dealer_register.PojoClass;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InboxDealer  {


    @SerializedName("QueryId")
    @Expose
    private Integer queryId;
    @SerializedName("CustID")
    @Expose
    private final Integer custID;
    @SerializedName("FirmName")
    @Expose
    private final String firmName;
    @SerializedName("MobileNumber")
    @Expose
    private final String mobileNumber;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("VillageLocalityname")
    @Expose
    private String villageLocalityname;
    @SerializedName("BusinessDemand")
    @Expose
    private final String businessDemand;
    @SerializedName("PurposeOfBusiness")
    @Expose
    private final String purposeOfBusiness;
    @SerializedName("Requirements")
    @Expose
    private final String requirements;
    @SerializedName("ProductPhoto")
    @Expose
    private final String productPhoto;

    @SerializedName("LastUpdatedMsgDate")
    @Expose
    private String LastUpdatedMsgDate;

    @SerializedName("SenderImage")
    private String senderImage;


    @SerializedName("EnquiryType")
    private String enquiryType;

    boolean isChecked;

    @SerializedName("IsRead")
    private int IsRead;

    @SerializedName("IsFavorite")
    @Expose
    private int IsFavorite;

    @SerializedName("ChildCategoryName")
    @Expose
    private String ChildCategoryName;

    @SerializedName("RequirementName")
    private String RequirementName;

//    @SerializedName("RequirementName")
//    private String RequirementName;

    @SerializedName("EnquiryDate")
    private String EnquiryDate;

    @SerializedName("isSentEnquiry")
    private int IsSentEnquiry;

    @SerializedName("TransactionType")
    private String TransactionType;

    @SerializedName("ProfessionalRequirementID")
    private int ProfessionalRequirementID;

    public InboxDealer(int queryId, int custID,String firmName, String mobileNumber, String emailID, String villageLocalityname, String businessDemand, String purposeOfBusiness, String requirements, String productPhoto,String lastUpdatedMsgDate,int isRead,String childCategoryName,int isFavorite,String enquiryType,String senderImage) {
        this.queryId = queryId;
        this.custID = custID;
        this.firmName = firmName;
        this.mobileNumber = mobileNumber;
        this.emailID = emailID;
        this.villageLocalityname = villageLocalityname;
        this.businessDemand = businessDemand;
        this.purposeOfBusiness = purposeOfBusiness;
        this.requirements = requirements;
        this.productPhoto = productPhoto;
        this.LastUpdatedMsgDate = lastUpdatedMsgDate;
        this.IsRead = isRead;
        this.ChildCategoryName = childCategoryName;
        this.IsFavorite = isFavorite;
        this.senderImage=senderImage;
        this.enquiryType=enquiryType;
    }



    public InboxDealer(Parcel in) {
        custID = in.readInt();
        firmName = in.readString();
        mobileNumber = in.readString();
        businessDemand = in.readString();
        purposeOfBusiness = in.readString();
        requirements = in.readString();
        productPhoto = in.readString();
        isChecked = in.readByte() != 0;
        senderImage=in.readString();
        enquiryType=in.readString();
    }

    public String getEnquiryDate() {
        return EnquiryDate;
    }

    public int getIsSentEnquiry() {
        return IsSentEnquiry;
    }

    public void setIsSentEnquiry(int isSentEnquiry) {
        IsSentEnquiry = isSentEnquiry;
    }

    public void setEnquiryDate(String enquiryDate) {
        EnquiryDate = enquiryDate;
    }

    public String getRequirementName() {
        return RequirementName;
    }

    public void setRequirementName(String requirementName) {
        RequirementName = requirementName;
    }

    public int getIsFavorite(){
        return IsFavorite;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }

    public String getLastUpdatedMsgDate() {
        return LastUpdatedMsgDate;
    }

    public Integer getQueryId() {
        return queryId;
    }

    public Integer getCustID() {
        return custID;
    }

    public String getFirmName() {
        return firmName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailID() {
        return emailID;
    }

    public String getVillageLocalityname() {
        return villageLocalityname;
    }

    public String getBusinessDemand() {
        return businessDemand;
    }

    public String getPurposeOfBusiness() {
        return purposeOfBusiness;
    }

    public String getRequirements() {
        return requirements;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(String enquiryType) {
        this.enquiryType = enquiryType;
    }



    public String getChildCategoryName() {
        return ChildCategoryName;
    }

    public void setChildCategoryName(String childCategoryName) {
        ChildCategoryName = childCategoryName;
    }
    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    @Override
    public String toString() {
        return firmName;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public int getProfessionalRequirementID() {
        return ProfessionalRequirementID;
    }

    public void setProfessionalRequirementID(int professionalRequirementID) {
        ProfessionalRequirementID = professionalRequirementID;
    }
}
