package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CustomerModel {

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("FirmName")
    private String FirmName;

    @SerializedName(value = "CityID", alternate = "CityId")
    private int CityID;

    @SerializedName("StateID")
    private int StateID;

    @SerializedName("StateName")
    private String stateName;

    @SerializedName("ProductID")
    private int ProductID;
    //static

    private String businessTypeForFilter;

    @SerializedName("businessTypes")
    private List<BusinessType> businessTypesList;

    public CustomerModel(int custID, int cityID, int stateID, int productID, List<BusinessType> businessTypeID) {
        CustID = custID;
        CityID = cityID;
        StateID = stateID;
        ProductID = productID;
        businessTypesList = businessTypeID;
    }
    public CustomerModel(int custID, int cityID, int stateID, List<BusinessType> businessTypeID) {
        CustID = custID;
        CityID = cityID;
        StateID = stateID;
        businessTypesList = businessTypeID;
    }

    public CustomerModel(int custID, String firmName) {
        CustID = custID;
        FirmName = firmName;
    }

    public CustomerModel(int custID, String firmName, int cityID) {
        CustID = custID;
        FirmName = firmName;
        CityID = cityID;
    }

    public CustomerModel(int custID) {
        CustID = custID;
    }

    private boolean isChecked=false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getCustID() {
        return CustID;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getFirmName() {
        return FirmName;
    }

    public void setFirmName(String firmName) {
        FirmName = firmName;
    }
    public List<BusinessType> getBusinessTypeID() {
        return businessTypesList;
    }

    public void setBusinessTypeID(List<BusinessType> businessTypeID) {
        businessTypesList = businessTypeID;
    }
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
    public String getBusinessTypeForFilter() {
        return businessTypeForFilter;
    }

    public void setBusinessTypeForFilter(String businessTypeForFilter) {
        this.businessTypeForFilter = businessTypeForFilter;
    }
    public String toString() {
        return "CustomerModel{" +
                "CustID =" + CustID +
                "FirmName=" + FirmName +
                "FilteredBTypes="+businessTypeForFilter+
                "businessTypes" +businessTypesList +
                "CityId=" + CityID +
                "StateID=" + StateID +
                "StateName=" + stateName;
    }
}
