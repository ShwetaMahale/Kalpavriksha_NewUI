package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewAdvertisementModule implements Serializable {

        @SerializedName("CustID")
        public int custID;
        @SerializedName("ProductID")
        public int productID;
        @SerializedName("BrandID")
        public int brandID;
        @SerializedName("BrandName")
        public String brandName;
        @SerializedName("AdvertisementTypeID")
        public int advertisementTypeID;
        //
        @SerializedName("AdvertisementType")
        public String advertisementType;
        @SerializedName("AdvertisementAreaLevel")
        public AdvertisementAreaLevel advertisementAreaLevel;
        @SerializedName("AdvertisementAreaLevelSubRegions")
        public List<String> advertisementAreaLevelSubRegions;
        @SerializedName("StartDate")
        public String startDate;
        @SerializedName("EndDate")
        public String endDate;
        @SerializedName("TimeSlots")
        public List<AdvertisementSlotModel> timeSlots;
        @SerializedName("RequestedIteration")
        public int requestedIteration;

        @SerializedName("ID")
        public int advertisementMainID;

       /* @SerializedName("AdvertisementMainID")
        public int advertisementMainID;*/

        @SerializedName("AdvertisementAreaName")
        public Object advertisementAreaName;

        @SerializedName("AdvertisementName")
        public String advertisementName;

        @SerializedName("AdvertisementAreaID")
        public int advertisementAreaID;
        @SerializedName("AdvertisementArea")
        public String advertisementArea;

        @SerializedName("CreatedDateStr")
        public String createdDateStr;

        @SerializedName("DispayMessage")
        private String DispayMessage="";

        @SerializedName("TaxValue")
        public double taxValue;

        @SerializedName("CGSTPer")
        public double cGSTPer;

        @SerializedName("SGSTPer")
        public double sGSTPer;

        @SerializedName("IGSTPer")
        public double iGSTPer;

        @SerializedName("CGSTAmount")
        public double cGSTAmount;

        @SerializedName("SGSTAmount")
        public double sGSTAmount;

        @SerializedName("IGSTAmount")
        public double iGSTAmount;

        @SerializedName("TaxAmount")
        public double taxAmount;

        @SerializedName("FinalPrice")
        public double finalPrice;

        @SerializedName("BookingExpiryDateStr")
        public String bookingExpiryDateStr;


        @SerializedName("TotalPrice")
        public double totalPrice;

        @SerializedName("TotalDiscountAmount")
        public double totalDiscountAmount;

        @SerializedName("TotalDays")
        public int TotalDays;

        @SerializedName("StatusCode")
        private int StatusCode;

        @SerializedName("CategoryProductID")
        private int CategoryProductID;

        @SerializedName("InvoiceUrl")
        public String InvoiceUrl;

        @SerializedName("States")
        private List<State> states;

        @SerializedName("Districts")
        private List<District> districts;

        @SerializedName("Cities")
        private List<CityAD> cities;

        @SerializedName("AdTotalPrice")
        public double AdTotalPrice;

        public NewAdvertisementModule(int advertisementMainID) {
                this.advertisementMainID = advertisementMainID;
        }

        public NewAdvertisementModule(int brandID, String brandName, String advertisementType, int advertisementMainID, String advertisementName, int advertisementAreaID, String advertisementArea, String createdDateStr, double cGSTPer, double sGSTPer, double iGSTPer, double cGSTAmount, double sGSTAmount, double iGSTAmount, double taxAmount, double finalPrice, String bookingExpiryDateStr, double totalPrice, double totalDiscountAmount, int totalDays, String invoiceUrl,double adTotalPrice) {
                this.brandID = brandID;
                this.brandName = brandName;
                this.advertisementType = advertisementType;
                this.advertisementMainID = advertisementMainID;
                this.advertisementName = advertisementName;
                this.advertisementAreaID = advertisementAreaID;
                this.advertisementArea = advertisementArea;
                this.createdDateStr = createdDateStr;
                this.cGSTPer = cGSTPer;
                this.sGSTPer = sGSTPer;
                this.iGSTPer = iGSTPer;
                this.cGSTAmount = cGSTAmount;
                this.sGSTAmount = sGSTAmount;
                this.iGSTAmount = iGSTAmount;
                this.taxAmount = taxAmount;
                this.finalPrice = finalPrice;
                this.bookingExpiryDateStr = bookingExpiryDateStr;
                this.totalPrice = totalPrice;
                this.totalDiscountAmount = totalDiscountAmount;
                TotalDays = totalDays;
                InvoiceUrl=invoiceUrl;
                AdTotalPrice=adTotalPrice;
        }


        public NewAdvertisementModule(int custID, int productID, int brandID, String brandName, int advertisementTypeID, String advertisementType, AdvertisementAreaLevel advertisementAreaLevel, List<String> advertisementAreaLevelSubRegions, String startDate, String endDate, List<AdvertisementSlotModel> timeSlots, int requestedIteration,int categoryProductID) {
                this.custID = custID;
                this.productID = productID;
                this.brandID = brandID;
                this.brandName = brandName;
                this.advertisementTypeID = advertisementTypeID;
                this.advertisementType = advertisementType;
                this.advertisementAreaLevel = advertisementAreaLevel;
                this.advertisementAreaLevelSubRegions = advertisementAreaLevelSubRegions;
                this.startDate = startDate;
                this.endDate = endDate;
                this.timeSlots = timeSlots;
                this.requestedIteration = requestedIteration;
                this.CategoryProductID=categoryProductID;
        }
        public NewAdvertisementModule(int custID, int productID, int brandID, String brandName, int advertisementTypeID, String advertisementType, AdvertisementAreaLevel advertisementAreaLevel, List<String> advertisementAreaLevelSubRegions, String startDate, String endDate, List<AdvertisementSlotModel> timeSlots, int requestedIteration,int categoryProductID,List<State> states, List<District> districts, List<CityAD> cities) {
                this.custID = custID;
                this.productID = productID;
                this.brandID = brandID;
                this.brandName = brandName;
                this.advertisementTypeID = advertisementTypeID;
                this.advertisementType = advertisementType;
                this.advertisementAreaLevel = advertisementAreaLevel;
                this.advertisementAreaLevelSubRegions = advertisementAreaLevelSubRegions;
                this.startDate = startDate;
                this.endDate = endDate;
                this.timeSlots = timeSlots;
                this.requestedIteration = requestedIteration;
                this.CategoryProductID=categoryProductID;
                this.states = states;
                this.districts = districts;
                this.cities = cities;
        }


        public int getCustID() {
                return custID;
        }

        public void setCustID(int custID) {
                this.custID = custID;
        }

        public int getProductID() {
                return productID;
        }

        public void setProductID(int productID) {
                this.productID = productID;
        }

        public int getBrandID() {
                return brandID;
        }

        public void setBrandID(int brandID) {
                this.brandID = brandID;
        }

        public String getBrandName() {
                return brandName;
        }

        public void setBrandName(String brandName) {
                this.brandName = brandName;
        }

        public int getAdvertisementTypeID() {
                return advertisementTypeID;
        }

        public void setAdvertisementTypeID(int advertisementTypeID) {
                this.advertisementTypeID = advertisementTypeID;
        }

        public String getAdvertisementType() {
                return advertisementType;
        }

        public void setAdvertisementType(String advertisementType) {
                this.advertisementType = advertisementType;
        }

        public AdvertisementAreaLevel getAdvertisementAreaLevel() {
                return advertisementAreaLevel;
        }

        public void setAdvertisementAreaLevel(AdvertisementAreaLevel advertisementAreaLevel) {
                this.advertisementAreaLevel = advertisementAreaLevel;
        }

        public List<String> getAdvertisementAreaLevelSubRegions() {
                return advertisementAreaLevelSubRegions;
        }

        public void setAdvertisementAreaLevelSubRegions(List<String> advertisementAreaLevelSubRegions) {
                this.advertisementAreaLevelSubRegions = advertisementAreaLevelSubRegions;
        }

        public String getStartDate() {
                return startDate;
        }

        public void setStartDate(String startDate) {
                this.startDate = startDate;
        }

        public String getEndDate() {
                return endDate;
        }

        public void setEndDate(String endDate) {
                this.endDate = endDate;
        }

        public List<AdvertisementSlotModel> getTimeSlots() {
                return timeSlots;
        }

        public void setTimeSlots(List<AdvertisementSlotModel> timeSlots) {
                this.timeSlots = timeSlots;
        }

        public int getRequestedIteration() {
                return requestedIteration;
        }

        public void setRequestedIteration(int requestedIteration) {
                this.requestedIteration = requestedIteration;
        }

        /*public int getiD() {
                return iD;
        }

        public void setiD(int iD) {
                this.iD = iD;
        }*/

        public int getAdvertisementMainID() {
                return advertisementMainID;
        }

        public void setAdvertisementMainID(int advertisementMainID) {
                this.advertisementMainID = advertisementMainID;
        }

        public Object getAdvertisementAreaName() {
                return advertisementAreaName;
        }

        public void setAdvertisementAreaName(Object advertisementAreaName) {
                this.advertisementAreaName = advertisementAreaName;
        }

        public String getAdvertisementName() {
                return advertisementName;
        }

        public void setAdvertisementName(String advertisementName) {
                this.advertisementName = advertisementName;
        }

        public int getAdvertisementAreaID() {
                return advertisementAreaID;
        }

        public void setAdvertisementAreaID(int advertisementAreaID) {
                this.advertisementAreaID = advertisementAreaID;
        }

        public String getAdvertisementArea() {
                return advertisementArea;
        }

        public void setAdvertisementArea(String advertisementArea) {
                this.advertisementArea = advertisementArea;
        }

        public String getCreatedDateStr() {
                return createdDateStr;
        }

        public void setCreatedDateStr(String createdDateStr) {
                this.createdDateStr = createdDateStr;
        }

        public String getDispayMessage() {
                return DispayMessage;
        }

        public void setDispayMessage(String dispayMessage) {
                DispayMessage = dispayMessage;
        }

        public double getTaxValue() {
                return taxValue;
        }

        public void setTaxValue(double taxValue) {
                this.taxValue = taxValue;
        }

        public double getcGSTPer() {
                return cGSTPer;
        }

        public void setcGSTPer(double cGSTPer) {
                this.cGSTPer = cGSTPer;
        }

        public double getsGSTPer() {
                return sGSTPer;
        }

        public void setsGSTPer(double sGSTPer) {
                this.sGSTPer = sGSTPer;
        }

        public double getiGSTPer() {
                return iGSTPer;
        }

        public void setiGSTPer(double iGSTPer) {
                this.iGSTPer = iGSTPer;
        }

        public double getcGSTAmount() {
                return cGSTAmount;
        }

        public void setcGSTAmount(double cGSTAmount) {
                this.cGSTAmount = cGSTAmount;
        }

        public double getsGSTAmount() {
                return sGSTAmount;
        }

        public void setsGSTAmount(double sGSTAmount) {
                this.sGSTAmount = sGSTAmount;
        }

        public double getiGSTAmount() {
                return iGSTAmount;
        }

        public void setiGSTAmount(double iGSTAmount) {
                this.iGSTAmount = iGSTAmount;
        }

        public double getTaxAmount() {
                return taxAmount;
        }

        public void setTaxAmount(double taxAmount) {
                this.taxAmount = taxAmount;
        }

        public double getFinalPrice() {
                return finalPrice;
        }

        public void setFinalPrice(double finalPrice) {
                this.finalPrice = finalPrice;
        }

        public String getBookingExpiryDateStr() {
                return bookingExpiryDateStr;
        }

        public void setBookingExpiryDateStr(String bookingExpiryDateStr) {
                this.bookingExpiryDateStr = bookingExpiryDateStr;
        }

        public double getTotalPrice() {
                return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
                this.totalPrice = totalPrice;
        }

        public double getTotalDiscountAmount() {
                return totalDiscountAmount;
        }

        public void setTotalDiscountAmount(double totalDiscountAmount) {
                this.totalDiscountAmount = totalDiscountAmount;
        }

        public int getTotalDays() {
                return TotalDays;
        }

        public void setTotalDays(int totalDays) {
                TotalDays = totalDays;
        }

        public int getStatusCode() {
                return StatusCode;
        }

        public void setStatusCode(int statusCode) {
                StatusCode = statusCode;
        }

        public int getCategoryProductID() {
                return CategoryProductID;
        }

        public void setCategoryProductID(int categoryProductID) {
                CategoryProductID = categoryProductID;
        }

        public String getInvoiceUrl() {
                return InvoiceUrl;
        }

        public void setInvoiceUrl(String invoiceUrl) {
                InvoiceUrl = invoiceUrl;
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

        public List<CityAD> getCities() {
                return cities;
        }

        public void setCities(List<CityAD> cities) {
                this.cities = cities;
        }

        public double getAdTotalPrice() {
                return AdTotalPrice;
        }

        public void setAdTotalPrice(double adTotalPrice) {
                AdTotalPrice = adTotalPrice;
        }
}
