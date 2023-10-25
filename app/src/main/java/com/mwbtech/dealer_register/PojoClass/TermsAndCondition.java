package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TermsAndCondition implements Serializable {
    @SerializedName("ID")
    private int ID;

    @SerializedName("TandC")
    private String TAndC="";

    @SerializedName("TAndCVersion")
    private String TAndCVersion;


    public TermsAndCondition(int ID, String TAndC) {
        this.ID = ID;
        this.TAndC = TAndC;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTAndC() {
        return TAndC;
    }

    public void setTAndC(String TAndC) {
        this.TAndC = TAndC;
    }

    public String getTAndCVersion() {
        return TAndCVersion;
    }

    public void setTAndCVersion(String TAndCVersion) {
        this.TAndCVersion = TAndCVersion;
    }
}
