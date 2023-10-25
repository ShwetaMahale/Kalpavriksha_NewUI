package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class Brand {
    @SerializedName("ID")
    private int BrandID;

    @SerializedName("BrandName")
    private String BrandName;

    public Brand(int brandID, String brandName) {
        BrandID = brandID;
        BrandName = brandName;
    }

    public int getBrandID() {
        return BrandID;
    }

    public void setBrandID(int brandID) {
        BrandID = brandID;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String brandName) {
        BrandName = brandName;
    }
}
