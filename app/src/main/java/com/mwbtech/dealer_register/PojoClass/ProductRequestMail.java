package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class ProductRequestMail {
    @SerializedName("CustID")
    private final int CustID;

    @SerializedName("SubCategoryName")
    private final String SubCategoryName;

    @SerializedName("ChildCategoryName")
    private final String ChildCategoryName;

    @SerializedName("ItemName")
    private final String ItemName;

    public ProductRequestMail(int custID, String subCategoryName, String childCategoryName, String itemName) {
        CustID = custID;
        SubCategoryName = subCategoryName;
        ChildCategoryName = childCategoryName;
        ItemName = itemName;
    }
}
