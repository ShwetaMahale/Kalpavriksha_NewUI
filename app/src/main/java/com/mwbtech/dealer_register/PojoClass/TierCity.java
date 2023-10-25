package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class TierCity {
    @SerializedName("ID")
    public int iD;
    @SerializedName("StatewithCityID")
    public int statewithCityID;
    @SerializedName("StateID")
    public int stateID;
    @SerializedName("DistrictID")
    public int districtID;
    @SerializedName("VillageLocalityName")
    public String villageLocalityName;
    @SerializedName("TairTypeOfCityID")
    public int tairTypeOfCityID;
    @SerializedName("TairTypeOfCity")
    public String tairTypeOfCity;

    public int getiD() {
        return iD;
    }

    public void setiD(int iD) {
        this.iD = iD;
    }

    public int getStatewithCityID() {
        return statewithCityID;
    }

    public void setStatewithCityID(int statewithCityID) {
        this.statewithCityID = statewithCityID;
    }

    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public int getDistrictID() {
        return districtID;
    }

    public void setDistrictID(int districtID) {
        this.districtID = districtID;
    }

    public String getVillageLocalityName() {
        return villageLocalityName;
    }

    public void setVillageLocalityName(String villageLocalityName) {
        this.villageLocalityName = villageLocalityName;
    }
}
