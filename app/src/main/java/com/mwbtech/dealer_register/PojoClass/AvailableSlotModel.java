package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class AvailableSlotModel implements Serializable {

    @SerializedName("AdvertisementMainID")
    private int AdvertisementMainID;

    @SerializedName("AdvertisementAreaID")
    private int AdvertisementAreaID;

    @SerializedName("ProductName")
    private String ProductName;

    @SerializedName("AdvertisementName")
    private String AdvertisementName;

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("TypeOfAdvertisementID")
    private int TypeOfAdvertisementID;

    @SerializedName("ProductID")
    private int ProductID;

    @SerializedName("BrandID")
    private int BrandID;

    @SerializedName("FromDate")
    private String FromDate;

    @SerializedName("ToDate")
    private String ToDate;

    @SerializedName("IntervalsPerHour")
    private int IntervalsPerHour;

    @SerializedName("advertisementStates")
    private List<State> stateList;

    @SerializedName("advertisementDistricts")
    private List<District> districtList;

    @SerializedName("advertisementCities")
    private List<City> cityList;

    @SerializedName("slots")
    private List<AdvertisementSlotModel> slotModelList;


    @SerializedName("CreatedBy")
    private int CreatedBy;

    @SerializedName("DispayMessage")
    private String DispayMessage="";

    @SerializedName("TotalPrice")
    private double TotalPrice;

    @SerializedName("TotalDiscountAmount")
    private double TotalDiscountAmount;

    @SerializedName("FinalPrice")
    private double FinalPrice;

    @SerializedName("TotalTaxAmount")
    private double TotalTaxAmount;

    @SerializedName("AdvertisementType")
    private String AdvertisementType;

    @SerializedName("CreatedDateStr")
    private String CreatedDateStr;

    @SerializedName("AdvertisementArea")
    private String AdvertisementArea;

    @SerializedName("TotalDays")
    private int DaysCount;

    @SerializedName("CGSTPer")
    private double CGSTPer;

    @SerializedName("SGSTPer")
    private double SGSTPer;

    @SerializedName("IGSTPer")
    private double IGSTPer;

    @SerializedName("CGSTAmount")
    private double CGSTAmount;

    @SerializedName("SGSTAmount")
    private double SGSTAmount;

    @SerializedName("IGSTAmount")
    private double IGSTAmount;

    @SerializedName("TaxAmount")
    private double TaxAmount;

    @SerializedName("BookingExpiryDateStr")
    private String BookingExpiryDateStr;

    @SerializedName("ApprovalStatus")
    private String ApprovalStatus;

    @SerializedName("PaymentStatus")
    private String PaymentStatus;

    @SerializedName("AdImageURL")
    private String AdImageURL;


    @SerializedName("States")
    private List<State> states;

    @SerializedName("Districts")
    private List<District> districts;

    @SerializedName("Cities")
    private List<City> cities;

    @SerializedName("TimeSlots")
    private List<AdvertisementSlotModel> slotModel;

    @SerializedName("AdText")
    private String AdText;

    @SerializedName("FromDateStr")
    private String FromDateStr;

    @SerializedName("ToDateStr")
    private String ToDateStr;

    @SerializedName("StatusCode")
    private int StatusCode;

    @SerializedName("Remarks")
    private String Remarks;

    @SerializedName("BrandName")
    private String BrandName;

    @SerializedName("PaymentDueDate")
    private String PaymentDueDate;

    @SerializedName("PaymentDetails")
    private List<PostPayment> paymentDetails;

    @SerializedName("IsMakePaymentAllowed")
    private boolean IsMakePaymentAllowed;

    public AvailableSlotModel(int advertisementMainID,int advertisementAreaID, String advertisementName, String advertisementType,
                              double totalDiscountAmount, double totalPrice,
                              double finalPrice, String createdDateStr,
                              String advertisementArea, int daysCount, double CGSTPer, double SGSTPer,
                              double IGSTPer, double CGSTAmount, double SGSTAmount, double IGSTAmount,
                              double taxAmount, String bookingExpiryDateStr,int brandID,String brandName) {
        AdvertisementMainID = advertisementMainID;
        AdvertisementName = advertisementName;
        AdvertisementType = advertisementType;
        TotalDiscountAmount = totalDiscountAmount;
        TotalPrice = totalPrice;
        FinalPrice = finalPrice;
        CreatedDateStr = createdDateStr;
        AdvertisementArea = advertisementArea;
        AdvertisementAreaID=advertisementAreaID;
        DaysCount = daysCount;
        this.CGSTPer = CGSTPer;
        this.SGSTPer = SGSTPer;
        this.IGSTPer = IGSTPer;
        this.CGSTAmount = CGSTAmount;
        this.SGSTAmount = SGSTAmount;
        this.IGSTAmount = IGSTAmount;
        TaxAmount = taxAmount;
        BookingExpiryDateStr = bookingExpiryDateStr;
        BrandID=brandID;
        BrandName=brandName;
    }

    public AvailableSlotModel(int advertisementMainID, double totalPrice, double totalDiscountAmount,
                              double finalPrice, double totalTaxAmount, String advertisementType) {
        AdvertisementMainID = advertisementMainID;
        TotalPrice = totalPrice;
        TotalDiscountAmount = totalDiscountAmount;
        FinalPrice = finalPrice;
        TotalTaxAmount = totalTaxAmount;
        AdvertisementType = advertisementType;
    }


    //full screen ad
    public AvailableSlotModel(int advertisementAreaID, String advertisementName, int custID,
                              int typeOfAdvertisementID, int productID, int brandID, String fromDate,
                              String toDate, List<State> stateList, List<District> districtList,
                              List<City> cityList,List<AdvertisementSlotModel> slotModelList, int createdBy,String brandName) {
        AdvertisementAreaID = advertisementAreaID;
        AdvertisementName = advertisementName;
        CustID = custID;
        TypeOfAdvertisementID = typeOfAdvertisementID;
        ProductID = productID;
        BrandID = brandID;
        FromDate = fromDate;
        ToDate = toDate;
        this.stateList = stateList;
        this.districtList = districtList;
        this.cityList = cityList;
        this.slotModelList = slotModelList;
        CreatedBy = createdBy;
        BrandName=brandName;
    }

    //text and banner ad
    public AvailableSlotModel(int advertisementAreaID, String advertisementName, int custID,
                              int typeOfAdvertisementID, int productID, int brandID, String fromDate,
                              String toDate, int intervalsPerHour, List<State> stateList,
                              List<District> districtList, List<City> cityList,
                              List<AdvertisementSlotModel> slotModelList, int createdBy,String brandName) {
        AdvertisementAreaID = advertisementAreaID;
        AdvertisementName = advertisementName;
        CustID = custID;
        TypeOfAdvertisementID = typeOfAdvertisementID;
        ProductID = productID;
        BrandID = brandID;
        FromDate = fromDate;
        ToDate = toDate;
        IntervalsPerHour = intervalsPerHour;
        this.stateList = stateList;
        this.districtList = districtList;
        this.cityList = cityList;
        this.slotModelList = slotModelList;
        CreatedBy = createdBy;
        BrandName=brandName;
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

    public int getAdvertisementAreaID() {
        return AdvertisementAreaID;
    }

    public void setAdvertisementAreaID(int advertisementAreaID) {
        AdvertisementAreaID = advertisementAreaID;
    }

    public String getAdvertisementName() {
        return AdvertisementName;
    }

    public void setAdvertisementName(String advertisementName) {
        AdvertisementName = advertisementName;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public int getTypeOfAdvertisementID() {
        return TypeOfAdvertisementID;
    }

    public void setTypeOfAdvertisementID(int typeOfAdvertisementID) {
        TypeOfAdvertisementID = typeOfAdvertisementID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
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

    public int getIntervalsPerHour() {
        return IntervalsPerHour;
    }

    public void setIntervalsPerHour(int intervalsPerHour) {
        IntervalsPerHour = intervalsPerHour;
    }

    public List<State> getStateList() {
        return stateList;
    }

    public void setStateList(List<State> stateList) {
        this.stateList = stateList;
    }

    public List<District> getDistrictList() {
        return districtList;
    }

    public void setDistrictList(List<District> districtList) {
        this.districtList = districtList;
    }

    public List<City> getCityList() {
        return cityList;
    }

    public void setCityList(List<City> cityList) {
        this.cityList = cityList;
    }

    public List<AdvertisementSlotModel> getSlotModelList() {
        return slotModelList;
    }

    public void setSlotModelList(List<AdvertisementSlotModel> slotModelList) {
        this.slotModelList = slotModelList;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getDispayMessage() {
        return DispayMessage;
    }

    public void setDispayMessage(String dispayMessage) {
        DispayMessage = dispayMessage;
    }

    public double getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        TotalPrice = totalPrice;
    }

    public double getTotalDiscountAmount() {
        return TotalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        TotalDiscountAmount = totalDiscountAmount;
    }

    public double getFinalPrice() {
        return FinalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        FinalPrice = finalPrice;
    }


    public double getTotalTaxAmount() {
        return TotalTaxAmount;
    }

    public void setTotalTaxAmount(double totalTaxAmount) {
        TotalTaxAmount = totalTaxAmount;
    }

    public String getAdvertisementType() {
        return AdvertisementType;
    }

    public void setAdvertisementType(String advertisementType) {
        AdvertisementType = advertisementType;
    }

    public String getCreatedDateStr() {
        return CreatedDateStr;
    }

    public void setCreatedDateStr(String createdDateStr) {
        CreatedDateStr = createdDateStr;
    }

    public String getAdvertisementArea() {
        return AdvertisementArea;
    }

    public void setAdvertisementArea(String advertisementArea) {
        AdvertisementArea = advertisementArea;
    }

    public int getDaysCount() {
        return DaysCount;
    }

    public void setDaysCount(int daysCount) {
        DaysCount = daysCount;
    }

    public double getCGSTPer() {
        return CGSTPer;
    }

    public void setCGSTPer(double CGSTPer) {
        this.CGSTPer = CGSTPer;
    }

    public double getSGSTPer() {
        return SGSTPer;
    }

    public void setSGSTPer(double SGSTPer) {
        this.SGSTPer = SGSTPer;
    }

    public double getIGSTPer() {
        return IGSTPer;
    }

    public void setIGSTPer(double IGSTPer) {
        this.IGSTPer = IGSTPer;
    }

    public double getCGSTAmount() {
        return CGSTAmount;
    }

    public void setCGSTAmount(double CGSTAmount) {
        this.CGSTAmount = CGSTAmount;
    }

    public double getSGSTAmount() {
        return SGSTAmount;
    }

    public void setSGSTAmount(double SGSTAmount) {
        this.SGSTAmount = SGSTAmount;
    }

    public double getIGSTAmount() {
        return IGSTAmount;
    }

    public void setIGSTAmount(double IGSTAmount) {
        this.IGSTAmount = IGSTAmount;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public String getBookingExpiryDateStr() {
        return BookingExpiryDateStr;
    }

    public void setBookingExpiryDateStr(String bookingExpiryDateStr) {
        BookingExpiryDateStr = bookingExpiryDateStr;
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

    public String getAdImageURL() {
        return AdImageURL;
    }

    public void setAdImageURL(String adImageURL) {
        AdImageURL = adImageURL;
    }

    public List<State> getStates() {
        return states;
    }

    public void setStates(List<State> states) {
        this.states = states;
    }

    public List<District> getDistricts() {
        return districts;
    }

    public void setDistricts(List<District> districts) {
        this.districts = districts;
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public List<AdvertisementSlotModel> getSlotModel() {
        return slotModel;
    }

    public void setSlotModel(List<AdvertisementSlotModel> slotModel) {
        this.slotModel = slotModel;
    }

    public String getAdText() {
        return AdText;
    }

    public void setAdText(String adText) {
        AdText = adText;
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

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }

    public AvailableSlotModel(int advertisementMainID) {
        AdvertisementMainID = advertisementMainID;
    }

    public AvailableSlotModel(int advertisementMainID, String adText) {
        AdvertisementMainID = advertisementMainID;
        AdText = adText;
    }

    public String getPaymentDueDate() {
        return PaymentDueDate;
    }

    public void setPaymentDueDate(String paymentDueDate) {
        PaymentDueDate = paymentDueDate;
    }

    public List<PostPayment> getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(List<PostPayment> paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public boolean isMakePaymentAllowed() {
        return IsMakePaymentAllowed;
    }

    public void setMakePaymentAllowed(boolean makePaymentAllowed) {
        IsMakePaymentAllowed = makePaymentAllowed;
    }

    @Override
    public String toString() {
        return "AvailableSlotModel{" +
                "AdvertisementAreaID=" + AdvertisementAreaID +
                ", AdvertisementName='" + AdvertisementName + '\'' +
                ", CustID=" + CustID +
                ", TypeOfAdvertisementID=" + TypeOfAdvertisementID +
                ", ProductID=" + ProductID +
                ", BrandID=" + BrandID +
                ", BrandName=" + BrandName +
                ", FromDate='" + FromDate + '\'' +
                ", ToDate='" + ToDate + '\'' +
                ", IntervalsPerHour=" + IntervalsPerHour +
                ", stateList=" + stateList +
                ", districtList=" + districtList +
                ", cityList=" + cityList +
                ", slotModelList=" + slotModelList +
                ", CreatedBy=" + CreatedBy +
                ", DispayMessage='" + DispayMessage + '\'' +
                ", TotalPrice=" + TotalPrice +
                ", TotalDiscountAmount=" + TotalDiscountAmount +
                '}';
    }
}
