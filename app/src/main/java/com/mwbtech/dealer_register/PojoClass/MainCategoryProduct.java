package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class MainCategoryProduct {

    @SerializedName("MainCategoryName")
    String CategoryName;
    boolean isChecked;
    @SerializedName("CategoryProductID")
    int CategoryId;
    int SubCategoryId;
    int CustID;

    public int getCustID() {
        return CustID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return CategoryName;
    }
}
