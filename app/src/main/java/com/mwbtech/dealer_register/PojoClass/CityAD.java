package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class CityAD implements Serializable {


    int StateID;
    private boolean checked;

    @SerializedName("ID")
    private int ID;

    @SerializedName("StateWithCityID")
    private int StatewithCityID;

    @SerializedName("VillageLocalityName")
    private String VillageLocalityname;

    public CityAD(int iD) {
        ID = iD;
    }

    public CityAD(int statewithCityID, String villageLocalityname, boolean checked) {
        StatewithCityID = statewithCityID;
        VillageLocalityname = villageLocalityname;
        this.checked = checked;
    }

    public CityAD(int statewithCityID, int stateID, String villageLocalityname) {
        StatewithCityID = statewithCityID;
        StateID = stateID;
        VillageLocalityname = villageLocalityname;
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

    @Override
    public String toString() {
        return  VillageLocalityname;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City)) return false;
        CityAD category = (CityAD) o;
        return StatewithCityID == category.StatewithCityID && VillageLocalityname.equals(category.VillageLocalityname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(StatewithCityID, VillageLocalityname);
    }
}