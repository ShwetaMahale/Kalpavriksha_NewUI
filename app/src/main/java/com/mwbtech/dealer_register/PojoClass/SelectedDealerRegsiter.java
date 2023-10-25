package com.mwbtech.dealer_register.PojoClass;

public class SelectedDealerRegsiter {

    int CustID;
    String CustomerName,BusinessType,MobileNumber;
    int SearchID;

    public SelectedDealerRegsiter(int custID, String customerName, String businessType, String mobileNumber, int searchID) {
        CustID = custID;
        CustomerName = customerName;
        BusinessType = businessType;
        MobileNumber = mobileNumber;
        SearchID = searchID;
    }


    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public int getSearchID() {
        return SearchID;
    }

    public void setSearchID(int searchID) {
        SearchID = searchID;
    }

    @Override
    public String toString() {
        return "SelectedDealerRegsiter{" +
                "CustID=" + CustID +
                ", CustomerName='" + CustomerName + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", MobileNumber='" + MobileNumber + '\'' +
                ", SearchID=" + SearchID +
                '}';
    }
}
