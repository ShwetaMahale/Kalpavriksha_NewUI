package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class OneToOneProductModel {
    @SerializedName("ItemId")
    private int ItemId;

    @SerializedName("ItemName")
    private String ItemName;

    @SerializedName("IsProfessional")
    private boolean IsProfessional;

    public boolean isProfessional() {
        return IsProfessional;
    }

    public void setProfessional(boolean professional) {
        IsProfessional = professional;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }
}
