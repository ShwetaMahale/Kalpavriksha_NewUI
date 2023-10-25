package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class AdvertisementAreaLevel {
    @SerializedName("ID")
    public int iD;
    @SerializedName("Name")
    public String name;
    @SerializedName("AdAreaMatrix")
    public double adAreaMatrix;

    public AdvertisementAreaLevel(int iD, String name, double adAreaMatrix) {
        this.iD = iD;
        this.name = name;
        this.adAreaMatrix = adAreaMatrix;
    }
}
