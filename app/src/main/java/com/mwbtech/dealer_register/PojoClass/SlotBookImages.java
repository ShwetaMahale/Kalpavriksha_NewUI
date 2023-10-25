package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SlotBookImages implements Serializable {
    public static final int IMAGE_TYPE=0;
    public static final int TEXT_TYPE=1;

    public int type;

    @SerializedName("Slot")
    public int slot;
    @SerializedName("ImageUrl")
    public String imageUrl;
    @SerializedName("AdText")
    public String adText;
    @SerializedName("AdvertisementMainID")
    public int advertisementMainID;
    @SerializedName("CustID")
    public int custID;
    @SerializedName("ProductName")
    public String productName;
    @SerializedName("CustomerInfo")
    public CustomerInfo customerInfo;
    @SerializedName("BusinessTypes")
    public BusinessType businessTypes;

    @SerializedName("IsCompanyAd")
    @Expose
    private Boolean IsCompanyAd;
    @SerializedName("PromoAd")
    @Expose
    private Boolean PromoAd;

    @SerializedName("ProductID")
    @Expose
    private int ProductID;

    @SerializedName("AdvertisementType")
    @Expose
    public String AdvertisementType;

    @SerializedName("AdvertisementName")
    @Expose
    public String AdvertisementName;

    @SerializedName("AdImageURL")
    @Expose
    public String AdImageURL;

    @SerializedName("ToDate")
    @Expose
    public String ToDate;

    @SerializedName("QuestionAnswered")
    private boolean QuestionAnswered;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public int getAdvertisementMainID() {
        return advertisementMainID;
    }

    public void setAdvertisementMainID(int advertisementMainID) {
        this.advertisementMainID = advertisementMainID;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public CustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getAdText() {
        return adText;
    }

    public void setAdText(String adText) {
        this.adText = adText;
    }

    public BusinessType getBusinessTypes() {
        return businessTypes;
    }

    public void setBusinessTypes(BusinessType businessTypes) {
        this.businessTypes = businessTypes;
    }


    public Boolean getCompanyAd() {
        return IsCompanyAd;
    }

    public void setCompanyAd(Boolean companyAd) {
        IsCompanyAd = companyAd;
    }

    public Boolean getPromoAd() {
        return PromoAd;
    }

    public void setPromoAd(Boolean promoAd) {
        PromoAd = promoAd;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getAdvertisementType() {
        return AdvertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        AdvertisementType = advertisementType;
    }

    public String getAdvertisementName() {
        return AdvertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        AdvertisementName = advertisementName;
    }

    public String getAdImageURL() {
        return AdImageURL;
    }

    public void setAdImageURL(String adImageURL) {
        AdImageURL = adImageURL;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public SlotBookImages(int type,String productName,int advertisementMainID,String advertisementName, String advertisementType,String toDate,String imageUrl, String adText) {
        this.type=type;
        this.productName = productName;
        this.advertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        ToDate = toDate;
        this.imageUrl = imageUrl;
        this.adText = adText;
    }
}


