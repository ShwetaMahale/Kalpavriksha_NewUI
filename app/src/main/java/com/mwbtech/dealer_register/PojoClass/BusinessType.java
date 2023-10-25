package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BusinessType implements Serializable {

    @SerializedName("ID")
    private int ID;
    @SerializedName("BusinessTypeID")
    private int BusinessTypeID;
    @SerializedName("Type")
    private String nameOfBusiness;
    @SerializedName("Selected")
    private String selectedImage;
    @SerializedName("UnSelected")
    private String unSelectedImage;
    @SerializedName("Checked")
    private boolean isChecked = false;
    private int CustID;


    public BusinessType(int businessTypeID) {
        BusinessTypeID = businessTypeID;
        ID = businessTypeID;

    }

    public BusinessType(int businessTypeID, String nameOfBusiness) {
        BusinessTypeID = businessTypeID;
        ID = businessTypeID;
        this.nameOfBusiness = nameOfBusiness;
    }
    public BusinessType(int businessTypeID, String nameOfBusiness, boolean isChecked) {
        BusinessTypeID = businessTypeID;
        ID = businessTypeID;
        this.nameOfBusiness = nameOfBusiness;
        this.isChecked = isChecked;
    }
    public BusinessType(int businessTypeID, String nameOfBusiness, int custID,boolean isChecked) {
        this.CustID = custID;
        BusinessTypeID = businessTypeID;
        ID = businessTypeID;
        this.nameOfBusiness = nameOfBusiness;
        this.isChecked = isChecked;
    }

    public BusinessType(int ID, int businessTypeID,int custID, String nameOfBusiness, boolean isChecked) {
        this.ID = ID;
        BusinessTypeID = businessTypeID;
        this.CustID = custID;
        this.nameOfBusiness = nameOfBusiness;
        this.isChecked = isChecked;
    }

    public BusinessType(int businessTypeID, String nameOfBusiness, String selectedImage, String unSelectedImage, int custID, boolean isChecked) {
        this.ID = BusinessTypeID;
        BusinessTypeID = businessTypeID;
        this.nameOfBusiness = nameOfBusiness;
        this.selectedImage = selectedImage;
        this.unSelectedImage = unSelectedImage;
        this.isChecked = isChecked;
        CustID = custID;
    }



    public int getCustID() {
        return CustID;
    }

    public int getBusinessTypeID() {
        return BusinessTypeID;
    }

    public String getNameOfBusiness() {
        return nameOfBusiness;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBusinessTypeID(int businessTypeID) {
        BusinessTypeID = businessTypeID;
    }

    public void setNameOfBusiness(String nameOfBusiness) {
        this.nameOfBusiness = nameOfBusiness;
    }

    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean checked) {
        isChecked = checked;
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
        return "BusinessType{" +
                "ID=" + ID +
                ", BusinessTypeID=" + BusinessTypeID +
                ", nameOfBusiness='" + nameOfBusiness + '\'' +
                ", selectedImage='" + selectedImage + '\'' +
                ", unSelectedImage='" + unSelectedImage + '\'' +
                ", isChecked=" + isChecked +
                ", CustID=" + CustID +
                '}';
    }
}
