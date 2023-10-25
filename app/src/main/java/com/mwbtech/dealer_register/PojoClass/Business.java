package com.mwbtech.dealer_register.PojoClass;

public class Business {

    int businessImage;
    String businessName;

    public Business(int businessImage, String businessName) {
        this.businessImage = businessImage;
        this.businessName = businessName;
    }

    public int getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(int businessImage) {
        this.businessImage = businessImage;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }


    @Override
    public String toString() {
        return "Business{" +
                "businessImage=" + businessImage +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
