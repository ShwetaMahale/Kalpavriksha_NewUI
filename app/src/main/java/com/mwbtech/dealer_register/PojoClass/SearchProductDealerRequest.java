package com.mwbtech.dealer_register.PojoClass;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SearchProductDealerRequest implements Serializable {

    @SerializedName("QueryID")
    private int QueryID;

    @SerializedName("ReceiverID")
    private int ReceiverID;

    @SerializedName("CustId")
    @Expose
    private Integer custId;

    @SerializedName("CityId")
    @Expose
    private Integer cityId;

    @SerializedName("CityIdList")
    @Expose
    private List<City> cityList;

    @SerializedName("ProductID")
    @Expose
    private Integer productID;
    @SerializedName("BusinessTypeIds")
    @Expose
    private List<BusinessType> businessTypeId = null;

    @SerializedName("BusinessDemand")
    private List<BusinessDemand> businessDemandList=null;

    @SerializedName("BusinessDemandID")
    private int BusinessDemandID;

    @SerializedName("TypeOfUse")
    @Expose
    private String typeOfUse;
    @SerializedName("Requirements")
    @Expose
    private String requirements;
    @SerializedName("ProductPhoto")
    @Expose
    private String productPhoto;
    @SerializedName("ProductPhoto2")
    @Expose
    private String productPhoto2;

    @SerializedName("FromDate")
    private String FromDate;

    @SerializedName("ToDate")
    private String ToDate;

    @SerializedName("IsSentEnquiry")
    private int IsSentEnquiry;

    @SerializedName("IsReceivedEnquiry")
    private int IsReceivedEnquiry;

    @SerializedName("ProfessionalRequirementID")
    private int ProfessionalRequirementID;

    @SerializedName("TransactionType")
    private String TransactionType;

    @SerializedName("PageNumber")
    private int pageNumber;

    @SerializedName("FilterBusinessTypeId")
    private int filterBusinessTypeId;

    @SerializedName("FilterBName_CityName")
    private String filterBNameCityName;


    private Integer CustID;
    private String  CustomerName;
    private String  BusinessType;
    private String MobileNumber;
    private Integer City;
    private String VillageLocalityname;
    private int IsFavorite;
    //private Integer SearchID;
    boolean isChecked;

    public SearchProductDealerRequest(Integer custId) {
        this.custId = custId;
    }

    public SearchProductDealerRequest(Integer custId, Integer cityId, List<City> selectedCities, Integer productID, List<BusinessType> businessTypeId, int  businessDemand, String typeOfUse, String requirements, String productPhoto, String productPhoto2, int professionalRequirementID, int pageNumber) {
        this.custId = custId;
        this.cityId = cityId;
        this.cityList = selectedCities;
        this.productID = productID;
        this.businessTypeId = businessTypeId;
        this.BusinessDemandID = businessDemand;
        this.typeOfUse = typeOfUse;
        this.requirements = requirements;
        this.productPhoto = productPhoto;
        this.productPhoto2 = productPhoto2;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.pageNumber = pageNumber;
    }

    public SearchProductDealerRequest(Integer custId, Integer cityId, List<City> selectedCities, Integer productID, List<BusinessType> businessTypeId, int  businessDemand, String typeOfUse, String requirements, String productPhoto, String productPhoto2, int professionalRequirementID, int pageNumber, int filterBusinessTypeId, String filterBNameCityName) {
        this.custId = custId;
        this.cityId = cityId;
        this.cityList = selectedCities;
        this.productID = productID;
        this.businessTypeId = businessTypeId;
        this.BusinessDemandID = businessDemand;
        this.typeOfUse = typeOfUse;
        this.requirements = requirements;
        this.productPhoto = productPhoto;
        this.productPhoto2 = productPhoto2;
        this.ProfessionalRequirementID = professionalRequirementID;
        this.pageNumber = pageNumber;
        this.filterBusinessTypeId = filterBusinessTypeId;
        this.filterBNameCityName = filterBNameCityName;
    }

    public SearchProductDealerRequest(Integer custID, String customerName, String businessType, String mobileNumber, Integer city, String VillageLocalityname, boolean isChecked) {
        CustID = custID;
        CustomerName = customerName;
        BusinessType = businessType;
        MobileNumber = mobileNumber;
        City = city;
        this.VillageLocalityname = VillageLocalityname;
       // SearchID = searchID;
        this.isChecked = isChecked;
    }


    public int getProfessionalRequirementID() {
        return ProfessionalRequirementID;
    }

    public void setProfessionalRequirementID(int professionalRequirementID) {
        ProfessionalRequirementID = professionalRequirementID;
    }

    protected SearchProductDealerRequest(Parcel in) {
        if (in.readByte() == 0) {
            custId = null;
        } else {
            custId = in.readInt();
        }
        if (in.readByte() == 0) {
            cityId = null;
        } else {
            cityId = in.readInt();
        }
        if (in.readByte() == 0) {
            productID = null;
        } else {
            productID = in.readInt();
        }
        //businessDemand = in.readString();
        typeOfUse = in.readString();
        requirements = in.readString();
        productPhoto = in.readString();
        productPhoto2 = in.readString();
        if (in.readByte() == 0) {
            CustID = null;
        } else {
            CustID = in.readInt();
        }
        CustomerName = in.readString();
        BusinessType = in.readString();
        MobileNumber = in.readString();
        if (in.readByte() == 0) {
            City = null;
        } else {
            City = in.readInt();
        }
        VillageLocalityname = in.readString();
        isChecked = in.readByte() != 0;
    }


    public int getBusinessDemandID() {
        return BusinessDemandID;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public String getProductPhoto2() {
        return productPhoto2;
    }

    public void setProductPhoto2(String productPhoto2) {
        this.productPhoto2 = productPhoto2;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public List<BusinessType> getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(List<BusinessType> businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public List<BusinessDemand> getBusinessDemandList() {
        return businessDemandList;
    }

    public void setBusinessDemandList(List<BusinessDemand> businessDemandList) {
        this.businessDemandList = businessDemandList;
    }

    public String getTypeOfUse() {
        return typeOfUse;
    }

    public void setTypeOfUse(String typeOfUse) {
        this.typeOfUse = typeOfUse;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getProductPhoto() {
        return productPhoto;
    }

    public void setProductPhoto(String productPhoto) {
        this.productPhoto = productPhoto;
    }


    public Integer getCustID() {
        return CustID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public Integer getCity() {
        return City;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public String getVillageLocalityname() {
        return VillageLocalityname;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(String transactionType) {
        TransactionType = transactionType;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getFilterBusinessTypeId() {
        return filterBusinessTypeId;
    }

    public void setFilterBusinessTypeId(int filterBusinessTypeId) {
        this.filterBusinessTypeId = filterBusinessTypeId;
    }

    public String getFilterBNameCityName() {
        return filterBNameCityName;
    }

    public void setFilterBNameCityName(String filterBNameCityName) {
        this.filterBNameCityName = filterBNameCityName;
    }

    @Override
    public String toString() {
        return "SearchProductDealer{" +
                "QueryID=" + QueryID +
                ", ReceiverID=" + ReceiverID +
                ", custId=" + custId +
                ", cityId=" + cityId +
                ", cityList=" + cityList +
                ", productID=" + productID +
                ", businessTypeId=" + businessTypeId +
                ", businessDemandList=" + businessDemandList +
                ", BusinessDemandID=" + BusinessDemandID +
                ", typeOfUse='" + typeOfUse + '\'' +
                ", requirements='" + requirements + '\'' +
                ", productPhoto='" + productPhoto + '\'' +
                ", productPhoto2='" + productPhoto2 + '\'' +
                ", FromDate='" + FromDate + '\'' +
                ", ToDate='" + ToDate + '\'' +
                ", IsSentEnquiry=" + IsSentEnquiry +
                ", IsReceivedEnquiry=" + IsReceivedEnquiry +
                ", CustID=" + CustID +
                ", CustomerName='" + CustomerName + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", MobileNumber='" + MobileNumber + '\'' +
                ", City=" + City +
                ", VillageLocalityname='" + VillageLocalityname + '\'' +
                ", IsFavorite=" + IsFavorite +
                ", isChecked=" + isChecked +
                ", pageNumber=" + pageNumber +
                '}';
    }

}
