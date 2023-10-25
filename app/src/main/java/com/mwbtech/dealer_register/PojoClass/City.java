package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class City implements Serializable {


    int StateID;
    private boolean checked;

    @SerializedName("ID")
    private int ID;

    @SerializedName("StateWithCityID")
    private int StatewithCityID;

    @SerializedName("VillageLocalityName")
    private String VillageLocalityname;

    @SerializedName("StateName")
    private String StateName;

    @SerializedName("PinCode")
    private int pinCode;

    /*public City(int iD) {
        ID = iD;
    }
    */

    public City(int statewithCityID) {
        StatewithCityID = statewithCityID;
    }

    public City(int statewithCityID, String villageLocalityname, boolean checked) {
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        this.checked = checked;
    }

    public City(int stateID, int statewithCityID, String villageLocalityname, String stateName) {
        StateID = stateID;
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
    }

    public City(int ID, int stateID, int statewithCityID, String villageLocalityname, String stateName) {
        this.ID = ID;
        StateID = stateID;
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
    }

    public City(int statewithCityID, int stateID, String villageLocalityname) {
        StatewithCityID = statewithCityID;
        StateID = stateID;
        VillageLocalityname = villageLocalityname;
    }

    public City(int ID, int stateID, int statewithCityID, String villageLocalityname, String stateName, int pinCode) {
        this.ID = ID;
        StateID = stateID;
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        StateName = stateName;
        this.pinCode = pinCode;
    }

    public int getStatewithCityID() {
        return StatewithCityID;
    }

    public int getStateID() {
        return StateID;
    }

    public String getVillageLocalityname() {
        return VillageLocalityname;
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

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
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
        if (!(o instanceof City)) return false;
        City category = (City) o;
        return StatewithCityID == category.StatewithCityID && VillageLocalityname.equals(category.VillageLocalityname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StatewithCityID, VillageLocalityname);
    }
}
