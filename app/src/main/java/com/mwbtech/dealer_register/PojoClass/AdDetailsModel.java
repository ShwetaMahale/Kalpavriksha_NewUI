package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AdDetailsModel implements Serializable {

    public static final int IMAGE_TYPE=0;
    public static final int TEXT_TYPE=1;

    public int type;

    public String fullscreenAd;


    @SerializedName("CustID")
    private int CustID;

    @SerializedName("ProductName")
    private String ProductName;

    @SerializedName("AdvertisementMainID")
    private int AdvertisementMainID;

    @SerializedName("AdvertisementName")
    private String AdvertisementName;

    @SerializedName("AdvertisementType")
    private String AdvertisementType;

    @SerializedName("CreatedDate")
    private String CreatedDate;

    @SerializedName("ApprovalStatus")
    private String ApprovalStatus;

    @SerializedName("PaymentStatus")
    private String PaymentStatus;

    @SerializedName("IsExpired")
    private boolean IsExpired;

    @SerializedName("ExpiryDateStr")
    private String ExpiryDateStr;

    @SerializedName("ProductID")
    private int ProductID;

    @SerializedName("FromDateStr")
    private String FromDateStr;

    @SerializedName("ToDateStr")
    private String ToDateStr;

    @SerializedName("FirmName")
    private String FirmName;

    @SerializedName("businessTypes")
    private List<BusinessType> businessTypes;

    @SerializedName("AdImageURL")
    private String AdImageURL;

    @SerializedName("AdText")
    private String AdText;

    @SerializedName("Remarks")
    private String Remarks;

    @SerializedName("customerInfo")
    private CustomerInfo customerInfo;

    //Payments
    @SerializedName("CustomerInfo")
    private CustomerInfo CustomerDetails;

    @SerializedName("IsCompanyAd")
    private boolean isCompanyAd;

    @SerializedName("IsMakePaymentAllowed")
    private boolean IsMakePaymentAllowed;

    @SerializedName("AdTotalPrice")
    private double AdTotalPrice;

    @SerializedName("TotalDiscount")
    private double TotalDiscount;

    @SerializedName("FinalPrice")
    private double FinalPrice;

    @SerializedName("TaxAmount")
    private double TaxAmount;

    @SerializedName("PaymentDueDate")
    private String PaymentDueDate;


    public AdDetailsModel(int type,String productName, int advertisementMainID, String advertisementName,
                          String advertisementType, String approvalStatus, String paymentStatus,
                          String adImageURL, String adText,String remarks,Boolean isMakePaymentAllowed,String paymentDueDate) {
        this.type=type;
        ProductName = productName;
        AdvertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        ApprovalStatus = approvalStatus;
        PaymentStatus = paymentStatus;
        AdImageURL = adImageURL;
        AdText = adText;
        Remarks=remarks;
        IsMakePaymentAllowed=isMakePaymentAllowed;
        PaymentDueDate=paymentDueDate;
    }

    public AdDetailsModel(int type,String productName, int advertisementMainID, String advertisementName,
                          String advertisementType, String approvalStatus, String paymentStatus,
                          String adImageURL, String adText,String remarks,Boolean isMakePaymentAllowed,String paymentDueDate,CustomerInfo customerDetails, double adTotalPrice, double totalDiscount, double finalPrice, double taxAmount) {
        this.type=type;
        ProductName = productName;
        AdvertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        ApprovalStatus = approvalStatus;
        PaymentStatus = paymentStatus;
        AdImageURL = adImageURL;
        AdText = adText;
        Remarks=remarks;
        IsMakePaymentAllowed=isMakePaymentAllowed;
        PaymentDueDate=paymentDueDate;
        CustomerDetails = customerDetails;
        AdTotalPrice = adTotalPrice;
        TotalDiscount = totalDiscount;
        FinalPrice = finalPrice;
        TaxAmount = taxAmount;
    }

    public AdDetailsModel(int type, String productName, int advertisementMainID, String advertisementName,
                          String advertisementType, String toDateStr,
                          String adImageURL, String adText, CustomerInfo customerDetails, int productID) {
        this.type=type;
        ProductName = productName;
        AdvertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        ToDateStr =toDateStr;
        AdImageURL = adImageURL;
        AdText = adText;
        CustomerDetails = customerDetails;
        ProductID = productID;
    }
    public AdDetailsModel(int type, String productName, int advertisementMainID, String advertisementName,
                          String advertisementType, String toDateStr,
                          String adImageURL, String adText) {
        this.type=type;
        ProductName = productName;
        AdvertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        ToDateStr =toDateStr;
        AdImageURL = adImageURL;
        AdText = adText;
    }
    //payment


    public String getAdText() {
        return AdText;
    }

    public void setAdText(String adText) {
        AdText = adText;
    }

    public String getAdImageURL() {
        return AdImageURL;
    }

    public void setAdImageURL(String adImageURL) {
        AdImageURL = adImageURL;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }

    public String getFromDateStr() {
        return FromDateStr;
    }

    public void setFromDateStr(String fromDateStr) {
        FromDateStr = fromDateStr;
    }

    public String getToDateStr() {
        return ToDateStr;
    }

    public void setToDateStr(String toDateStr) {
        ToDateStr = toDateStr;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public List<BusinessType> getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(List<BusinessType> businessTypes) {
        this.businessTypes = businessTypes;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getExpiryDateStr() {
        return ExpiryDateStr;
    }

    public void setExpiryDateStr(String expiryDateStr) {
        ExpiryDateStr = expiryDateStr;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getAdvertisementMainID() {
        return AdvertisementMainID;
    }

    public void setAdvertisementMainID(int advertisementMainID) {
        AdvertisementMainID = advertisementMainID;
    }

    public String getAdvertisementName() {
        return AdvertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        AdvertisementName = advertisementName;
    }

    public String getAdvertisementType() {
        return AdvertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        AdvertisementType = advertisementType;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getApprovalStatus() {
        return ApprovalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        ApprovalStatus = approvalStatus;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public boolean isExpired() {
        return IsExpired;
    }

    public void setExpired(boolean expired) {
        IsExpired = expired;
    }


    public String getFullscreenAd() {
        return fullscreenAd;
    }

    public void setFullscreenAd(String fullscreenAd) {
        this.fullscreenAd = fullscreenAd;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public boolean isCompanyAd() {
        return isCompanyAd;
    }

    public void setCompanyAd(boolean companyAd) {
        isCompanyAd = companyAd;
    }

    public CustomerInfo getCustomerDetails() {
        return CustomerDetails;
    }

    public void setCustomerDetails(CustomerInfo customerDetails) {
        CustomerDetails = customerDetails;
    }

    public boolean isMakePaymentAllowed() {
        return IsMakePaymentAllowed;
    }

    public void setMakePaymentAllowed(boolean makePaymentAllowed) {
        IsMakePaymentAllowed = makePaymentAllowed;
    }

    public double getAdTotalPrice() {
        return AdTotalPrice;
    }

    public void setAdTotalPrice(double adTotalPrice) {
        AdTotalPrice = adTotalPrice;
    }

    public double getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        TotalDiscount = totalDiscount;
    }

    public double getFinalPrice() {
        return FinalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        FinalPrice = finalPrice;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public String getPaymentDueDate() {
        return PaymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        PaymentDueDate = paymentDueDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

