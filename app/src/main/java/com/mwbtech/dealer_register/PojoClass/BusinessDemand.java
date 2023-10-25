package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusinessDemand implements Serializable {
    @SerializedName("Demand")
    private String BusinessDemand;
    @SerializedName("Selected")
    private String selectedImage;
    @SerializedName("UnSelected")
    private String unSelectedImage;

    @SerializedName("BusinessDemandID")
    private int BusinessDemandID;

    @SerializedName("ID")
    private int demandId;

    @SerializedName("Checked")
    private boolean checked;


    private int CustID;

    public BusinessDemand(String businessDemand, String selectedImage, String unSelectedImage, int businessDemandID, boolean checked, int custID) {
        BusinessDemand = businessDemand;
        this.selectedImage = selectedImage;
        this.unSelectedImage = unSelectedImage;
        BusinessDemandID = businessDemandID;
        this.checked = checked;
        CustID = custID;
    }

    public BusinessDemand(int businessDemandID) {
        BusinessDemandID = businessDemandID;
    }

    public BusinessDemand(String businessDemand, int businessDemandID, boolean checked) {
        BusinessDemand = businessDemand;
        BusinessDemandID = businessDemandID;
        this.checked = checked;
    }

    public BusinessDemand(String businessDemand, int businessDemandID) {
        BusinessDemand = businessDemand;
        BusinessDemandID = businessDemandID;
    }

    public BusinessDemand(String businessDemand, int businessDemandID, boolean checked, int custID) {
        BusinessDemand = businessDemand;
        BusinessDemandID = businessDemandID;
        this.checked = checked;
        CustID = custID;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getBusinessDemand() {
        return BusinessDemand;
    }

    public void setBusinessDemand(String businessDemand) {
        BusinessDemand = businessDemand;
    }

    public int getBusinessDemandID() {
        return BusinessDemandID;
    }

    public void setBusinessDemandID(int businessDemandID) {
        BusinessDemandID = businessDemandID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(String selectedImage) {
        this.selectedImage = selectedImage;
    }

    public String getUnSelectedImage() {
        return unSelectedImage;
    }

    public void setUnSelectedImage(String unSelectedImage) {
        this.unSelectedImage = unSelectedImage;
    }

    @Override
    public String toString() {
        return "BusinessDemand{" +
                "BusinessDemand='" + BusinessDemand + '\'' +
                ", BusinessDemandID=" + BusinessDemandID +
                ", selectedImage=" + selectedImage +
                ", unSelectedImage=" + unSelectedImage +
                ", CustID=" + CustID +
                ", Checked=" + checked +
                '}';
    }
}
