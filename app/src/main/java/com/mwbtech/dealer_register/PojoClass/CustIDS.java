package com.mwbtech.dealer_register.PojoClass;

public class CustIDS {
    int CustID;
    int CityId;

    public CustIDS(int custID, int cityId){
        this.CustID = custID;
        this.CityId = cityId;
    }

    public int getCustID() {
        return CustID;
    }


    @Override
    public String toString() {
        return "CustID=" + CustID;
    }
}
