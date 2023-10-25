package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class SubCategoryProduct {
    //SubCategoryId
    @SerializedName("SubCategoryId")
    int SubCategoryId;
    @SerializedName("SubCategoryName")
    String SubCategoryName;
    @SerializedName("IsChecked")
    boolean isChecked;

    @SerializedName("MainCategoryName")
    String Catname;

    int CustID;
    @SerializedName("CategoryProductID")
    int CategoryProductId;

    public SubCategoryProduct(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public SubCategoryProduct(int subCategoryId, String subCategoryName) {
        SubCategoryId = subCategoryId;
        SubCategoryName = subCategoryName;
    }


    public SubCategoryProduct(int CustID, int CategoryProductId, int subCategoryId, String subCategoryName, boolean ischecked) {
        this.CustID = CustID;
        this.CategoryProductId = CategoryProductId;
        SubCategoryId = subCategoryId;
        SubCategoryName = subCategoryName;
        isChecked = ischecked;
    }

    public String getCatname() {
        return Catname;
    }

    public void setCatname(String catname) {
        Catname = catname;
    }

    public int getCategoryProductId() {
        return CategoryProductId;
    }

    public int getCustID() {
        return CustID;
    }


    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return SubCategoryName;
    }

}
