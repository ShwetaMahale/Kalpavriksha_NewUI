package com.mwbtech.dealer_register.PojoClass;

public class SearchList {


    private final Integer CustID;
    private final String  CustomerName;
    private final String  BusinessType;
    private final String MobileNumber;
    private final Integer City;
    private final String VillageLocalityname;
    private final Integer SearchID;
    boolean isChecked;


    public SearchList(Integer custID, String customerName, String businessType, String mobileNumber, Integer city, String VillageLocalityname,Integer searchID, boolean isChecked) {
        CustID = custID;
        CustomerName = customerName;
        BusinessType = businessType;
        MobileNumber = mobileNumber;
        City = city;
        this.VillageLocalityname = VillageLocalityname;
        SearchID = searchID;
        this.isChecked = isChecked;
    }


    public Integer getCustID() {
        return CustID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public Integer getCity() {
        return City;
    }

    public Integer getSearchID() {
        return SearchID;
    }

    public boolean isChecked() {
        return isChecked;
    }


    public String getVillageLocalityname() {
        return VillageLocalityname;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

}
