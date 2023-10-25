package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class StateCityResponse implements Serializable {

    private boolean checked;

    @SerializedName("ID")
    private int ID;

    @SerializedName("StateWithCityID")
    private int StatewithCityID;

    @SerializedName("StateID")
    private int StateID;

    @SerializedName("DistrictID")
    private int DistrictID;

    @SerializedName("VillageLocalityName")
    private String VillageLocalityname;

    @SerializedName("StateName")
    private String StateName;

    @SerializedName("DistrictName")
    private String DistrictName;

    @SerializedName("PinCode")
    private int pinCode;

    /*public City(int iD) {
        ID = iD;
    }
    */

    public StateCityResponse(int statewithCityID) {
        StatewithCityID = statewithCityID;
    }

    public StateCityResponse(int statewithCityID, String villageLocalityname, boolean checked) {
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        this.checked = checked;
    }

    public StateCityResponse(int stateID, int statewithCityID, String villageLocalityname, String stateName) {
        StateID = stateID;
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
    }

    public StateCityResponse(int ID, int statewithCityID, int stateID, int districtID, String villageLocalityname, String stateName, String districtName) {
        this.ID = ID;
        StatewithCityID = statewithCityID;
        StateID = stateID;
        DistrictID = districtID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
        DistrictName = districtName;
    }

    public StateCityResponse(int statewithCityID, int stateID, String villageLocalityname) {
        StatewithCityID = statewithCityID;
        StateID = stateID;
        VillageLocalityname = villageLocalityname;
    }

    public StateCityResponse(int ID, int statewithCityID, int stateID, int districtID, String villageLocalityname, String stateName, String districtName, int pinCode) {
        this.ID = ID;
        StatewithCityID = statewithCityID;
        StateID = stateID;
        DistrictID = districtID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
        DistrictName = districtName;
        this.pinCode = pinCode;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getStatewithCityID() {
        return StatewithCityID;
    }

    public void setStatewithCityID(int statewithCityID) {
        StatewithCityID = statewithCityID;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public int getDistrictID() {
        return DistrictID;
    }

    public void setDistrictID(int districtID) {
        DistrictID = districtID;
    }

    public String getVillageLocalityname() {
        return VillageLocalityname;
    }

    public void setVillageLocalityname(String villageLocalityname) {
        VillageLocalityname = villageLocalityname;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getDistrictName() {
        return DistrictName;
    }

    public void setDistrictName(String districtName) {
        DistrictName = districtName;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return  VillageLocalityname;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StateCityResponse)) return false;
        StateCityResponse category = (StateCityResponse) o;
        return StatewithCityID == category.StatewithCityID && VillageLocalityname.equals(category.VillageLocalityname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StatewithCityID, VillageLocalityname);
    }
}
