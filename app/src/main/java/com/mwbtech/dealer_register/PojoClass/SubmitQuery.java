package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SubmitQuery {

    int QueryId;
    List<CustIDS> CustIDS;
    int CustID;
    int CityId;
    int ProductID;
    List<BusinessType> BusinessTypeIds;
    int BusinessDemandID;
    String TypeOfUse;
    String Requirements;
    String ProductPhoto;
    String ProductPhoto2;

    List<CustomerModel> customerModels;

    @SerializedName("IsAdEnquiry")
    private final boolean IsAdEnquiry;

    @SerializedName("ProfessionalRequirementID")
    private final int ProfessionalRequirementID;

    @SerializedName("EnquiryType")
    private final String EnquiryType;

    @SerializedName("TransactionType")
    private String TransactionType;

    public SubmitQuery(int queryId, List<CustIDS> custIDSList, int custID, int cityId, int productID,
                       List<BusinessType> businessTypeIds, int businessDemand, String typeOfUse,
                       String requirements, String productPhoto,String productPhoto2,int professionalRequirementID,boolean isAdEnq,String EnqType) {
        QueryId = queryId;
        this.CustIDS = custIDSList;
        CustID = custID;
        CityId = cityId;
        ProductID = productID;
        BusinessTypeIds = businessTypeIds;
        BusinessDemandID = businessDemand;
        TypeOfUse = typeOfUse;
        Requirements = requirements;
        ProductPhoto = productPhoto;
        ProductPhoto2 = productPhoto2;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.IsAdEnquiry = isAdEnq;
        this.EnquiryType = EnqType;
    }
    public SubmitQuery(int queryId, List<CustIDS> custIDSList, int custID, int cityId, int productID,
                       List<BusinessType> businessTypeIds, int businessDemand, String typeOfUse,
                       String requirements, String productPhoto,String productPhoto2,int professionalRequirementID,boolean isAdEnq,String EnqType,String transactionType) {
        QueryId = queryId;
        this.CustIDS = custIDSList;
        CustID = custID;
        CityId = cityId;
        ProductID = productID;
        BusinessTypeIds = businessTypeIds;
        BusinessDemandID = businessDemand;
        TypeOfUse = typeOfUse;
        Requirements = requirements;
        ProductPhoto = productPhoto;
        ProductPhoto2 = productPhoto2;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.IsAdEnquiry = isAdEnq;
        this.EnquiryType = EnqType;
        this.TransactionType=transactionType;
    }

    public SubmitQuery(int queryId, List<CustIDS> custIDSList, int custID, int cityId, int productID,
                       List<BusinessType> businessTypeIds, int businessDemand, String typeOfUse,
                       String requirements,int professionalRequirementID,boolean isAdEnq,String EnqType,String transactionType) {
        QueryId = queryId;
        CustIDS = custIDSList;
        CustID = custID;
        CityId = cityId;
        ProductID = productID;
        BusinessTypeIds = businessTypeIds;
        BusinessDemandID = businessDemand;
        TypeOfUse = typeOfUse;
        Requirements = requirements;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.IsAdEnquiry = isAdEnq;
        this.EnquiryType = EnqType;
        this.TransactionType=transactionType;
    }
    public SubmitQuery(int queryId, List<CustIDS> custIDSList, int custID, int cityId, int productID,
                       List<BusinessType> businessTypeIds, int businessDemand, String typeOfUse,
                       String requirements,int professionalRequirementID,boolean isAdEnq,String EnqType) {
        QueryId = queryId;
        CustIDS = custIDSList;
        CustID = custID;
        CityId = cityId;
        ProductID = productID;
        BusinessTypeIds = businessTypeIds;
        BusinessDemandID = businessDemand;
        TypeOfUse = typeOfUse;
        Requirements = requirements;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.IsAdEnquiry = isAdEnq;
        this.EnquiryType = EnqType;

    }


    public int getQueryId() {
        return QueryId;
    }

    public List<CustIDS> getCustIDSList() {
        return CustIDS;
    }

    public int getCustID() {
        return CustID;
    }

    public int getCityId() {
        return CityId;
    }

    public int getProductID() {
        return ProductID;
    }

    public List<BusinessType> getBusinessTypeIds() {
        return BusinessTypeIds;
    }

    public int getBusinessDemandID() {
        return BusinessDemandID;
    }

    public String getTypeOfUse() {
        return TypeOfUse;
    }

    public String getRequirements() {
        return Requirements;
    }

    public String getProductPhoto() {
        return ProductPhoto;
    }

    public String getProductPhoto2() {
        return ProductPhoto2;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    @Override
    public String toString() {
        return "SubmitQuery{" +
                "QueryId=" + QueryId +
                ", CustIDS=" + customerModels +
                ", CustID=" + CustID +
                ", CityId=" + CityId +
                ", ProductID=" + ProductID +
                ", BusinessTypeIds=" + BusinessTypeIds +
                ", BusinessDemandID='" + BusinessDemandID + '\'' +
                ", TypeOfUse='" + TypeOfUse + '\'' +
                ", Requirements='" + Requirements + '\'' +
                ", ProductPhoto='" + ProductPhoto + '\'' +
                ", ProductPhoto2='" + ProductPhoto2 + '\'' +
                ", EnquiryType = '" + EnquiryType+'\''+
                '}';
    }
}
