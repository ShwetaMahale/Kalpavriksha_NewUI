package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class OutboxDealer {
   @SerializedName("QueryId")
    private int QueryId;

   @SerializedName("CityId")
    private int CityId;

    @SerializedName("EnquiryType")
    private String EnquiryType;


    @SerializedName("SenderImage")
    private String senderImage;


    @SerializedName("VillageLocalityname")
    private String VillageLocalityname;

   @SerializedName("BusinessDemand")
    private String BusinessDemand;

   @SerializedName("PurposeOfBusiness")
    private String PurposeOfBusiness;

   @SerializedName("LastUpdatedMsgDate")
    private String LastUpdatedMsgDate;

   @SerializedName("ChildCategoryName")
    private String ChildCategoryName;

   @SerializedName("ReplyCount")
    private int ReplyCount;

   @SerializedName("RequirementName")
   private String RequirementName;

    @SerializedName("EnquiryDate")
    private String EnquiryDate;

    @SerializedName("TransactionType")
    private String TransactionType;

    @SerializedName("ProfessionalRequirementID")
    private int ProfessionalRequirementID;

    @SerializedName("SelectedCities")
    private String selectedCities;

    public String getEnquiryDate() {
        return EnquiryDate;
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

    public int getQueryId() {
        return QueryId;
    }

    public void setQueryId(int queryId) {
        QueryId = queryId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public String getVillageLocalityname() {
        return VillageLocalityname;
    }

    public void setVillageLocalityname(String villageLocalityname) {
        VillageLocalityname = villageLocalityname;
    }

    public String getBusinessDemand() {
        return BusinessDemand;
    }

    public void setBusinessDemand(String businessDemand) {
        BusinessDemand = businessDemand;
    }

    public String getPurposeOfBusiness() {
        return PurposeOfBusiness;
    }

    public void setPurposeOfBusiness(String purposeOfBusiness) {
        PurposeOfBusiness = purposeOfBusiness;
    }

    public String getLastUpdatedMsgDate() {
        return LastUpdatedMsgDate;
    }

    public void setLastUpdatedMsgDate(String lastUpdatedMsgDate) {
        LastUpdatedMsgDate = lastUpdatedMsgDate;
    }

    public String getChildCategoryName() {
        return ChildCategoryName;
    }

    public void setChildCategoryName(String childCategoryName) {
        ChildCategoryName = childCategoryName;
    }

    public int getReplyCount() {
        return ReplyCount;
    }
    public String getEnquiryType() {
        return EnquiryType;
    }

    public void setReplyCount(int replyCount) {
        ReplyCount = replyCount;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
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

    public String getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(String selectedCities) {
        this.selectedCities = selectedCities;
    }
}
