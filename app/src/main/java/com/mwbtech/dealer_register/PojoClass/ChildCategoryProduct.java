package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class ChildCategoryProduct {

    @SerializedName("ChildCategoryId")
    int childCategoryId;
    @SerializedName("ChildCategoryName")
    String childCategoryName;
    @SerializedName("CategoryProductID")
    int CategoryProductID;
    @SerializedName("IsProfessional")
    boolean IsProfessional;
    boolean isChecked;
    private int CustID;
    @SerializedName("SubCategoryId")
    int SubCategoryProductId;

    @SerializedName("MainCategoryName")
    private String MainCategoryName;


    public ChildCategoryProduct(String childCategoryName) {
        this.childCategoryName = childCategoryName;
    }

    public ChildCategoryProduct(int SubCategoryProductId, int childCategoryId, String childCategoryName) {
        this.SubCategoryProductId = SubCategoryProductId;
        this.childCategoryId = childCategoryId;
        this.childCategoryName = childCategoryName;
    }

    public ChildCategoryProduct(int childCategoryId, String childCategoryName,boolean isChecked) {
        this.childCategoryId = childCategoryId;
        this.childCategoryName = childCategoryName;
        this.isChecked = isChecked;
    }

    public ChildCategoryProduct(int CustID, int SubCategoryProductId, int childCategoryId, String childCategoryName, boolean isChecked) {
        this.CustID = CustID;
        this.SubCategoryProductId = SubCategoryProductId;
        this.childCategoryId = childCategoryId;
        this.childCategoryName = childCategoryName;
        this.isChecked = isChecked;
    }

    public ChildCategoryProduct(int childCategoryId, String childCategoryName, int categoryProductID, boolean isChecked, int custID, int subCategoryProductId) {
        this.childCategoryId = childCategoryId;
        this.childCategoryName = childCategoryName;
        CategoryProductID = categoryProductID;
        this.isChecked = isChecked;
        CustID = custID;
        SubCategoryProductId = subCategoryProductId;
    }

    public String getMainCategoryName() {
        return MainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        MainCategoryName = mainCategoryName;
    }

    public int getCustID() {
        return CustID;
    }
    public int getChildCategoryId() {
        return childCategoryId;
    }

    public String getChildCategoryName() {
        return childCategoryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public boolean isProfessional() {
        return IsProfessional;
    }

    public void setProfessional(boolean professional) {
        IsProfessional = professional;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getSubCategoryProductId() {
        return SubCategoryProductId;
    }

    @Override
    public String toString() {
        return childCategoryName;
    }

    public int getCategoryProductID() {
        return CategoryProductID;
    }

    public void setCategoryProductID(int categoryProductID) {
        CategoryProductID = categoryProductID;
    }
}
