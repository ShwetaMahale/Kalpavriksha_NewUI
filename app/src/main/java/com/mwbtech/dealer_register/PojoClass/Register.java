package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Register {

    @SerializedName("CustID")
    @Expose
    private Integer custID;
    @SerializedName("FirmName")
    @Expose
    private String firmName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("UserType")
    @Expose
    private String userType;
    @SerializedName("CustName")
    @Expose
    private String custName;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("MobileNumber2")
    @Expose
    private String mobileNumber2;
    @SerializedName("TelephoneNumber")
    @Expose
    private String telephoneNumber;
    @SerializedName("ShopImage")
    @Expose
    private String shopImage;
    @SerializedName("BillingAddress")
    @Expose
    private String billingAddress;
    @SerializedName("Area")
    @Expose
    private String area;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("CityCode")
    @Expose
    private String cityCode;
    @SerializedName("Satate")
    @Expose
    private String satate;
    @SerializedName("Pincode")
    @Expose
    private String pincode;
    @SerializedName("Lattitude")
    @Expose
    private String lattitude;
    @SerializedName("Langitude")
    @Expose
    private String langitude;
    @SerializedName("InterstCity")
    @Expose
    private Boolean interstCity;
    @SerializedName("InterstState")
    @Expose
    private Boolean interstState;
    @SerializedName("InterstCountry")
    @Expose
    private Boolean interstCountry;
    @SerializedName("RegistrationType")
    @Expose
    private String registrationType;
    @SerializedName("TinNumber")
    @Expose
    private String tinNumber;
    @SerializedName("PanNumber")
    @Expose
    private String panNumber;
    @SerializedName("Bankname")
    @Expose
    private String bankname;
    @SerializedName("BankBranchName")
    @Expose
    private String bankBranchName;
    @SerializedName("Accountnumber")
    @Expose
    private String accountnumber;
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode;
    @SerializedName("BankCity")
    @Expose
    private String bankCity;
    @SerializedName("SalesmanID")
    @Expose
    private Integer salesmanID;
    @SerializedName("CreatedByID")
    @Expose
    private Integer createdByID;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("IsRegistered")
    @Expose
    private Integer isRegistered;
    @SerializedName("BusinessTypeWithCust")
    @Expose
    private List<BusinessType> businessTypeWithCust = null;
    @SerializedName("CategoryTypeWithCust")
    @Expose
    private List<MainCategoryProduct> categoryTypeWithCust = null;
    @SerializedName("SubCategoryTypeWithCust")
    @Expose
    private List<SubCategoryProduct> subCategoryTypeWithCust = null;
    @SerializedName("ChildCategoryTypeWithCust")
    @Expose
    private List<ChildCategoryProduct> childCategoryTypeWithCust = null;


    public Register(Integer custID, String firmName, String password, String userType, String custName, String emailID, String mobileNumber, String mobileNumber2, String telephoneNumber, String shopImage, String billingAddress, String area, String city, String cityCode, String satate, String pincode, String lattitude, String langitude, Boolean interstCity, Boolean interstState, Boolean interstCountry, String registrationType, String tinNumber, String panNumber, String bankname, String bankBranchName, String accountnumber, String iFSCCode, String bankCity, Integer salesmanID, Integer createdByID, String createdDate, Integer isRegistered, List<BusinessType> businessTypeWithCust, List<MainCategoryProduct> categoryTypeWithCust, List<SubCategoryProduct> subCategoryTypeWithCust, List<ChildCategoryProduct> childCategoryTypeWithCust) {
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.satate = satate;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.interstCity = interstCity;
        this.interstState = interstState;
        this.interstCountry = interstCountry;
        this.registrationType = registrationType;
        this.tinNumber = tinNumber;
        this.panNumber = panNumber;
        this.bankname = bankname;
        this.bankBranchName = bankBranchName;
        this.accountnumber = accountnumber;
        this.iFSCCode = iFSCCode;
        this.bankCity = bankCity;
        this.salesmanID = salesmanID;
        this.createdByID = createdByID;
        this.createdDate = createdDate;
        this.isRegistered = isRegistered;
        this.businessTypeWithCust = businessTypeWithCust;
        this.categoryTypeWithCust = categoryTypeWithCust;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.childCategoryTypeWithCust = childCategoryTypeWithCust;
    }

    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public String getFirmName() {
        return firmName;
    }

    public void setFirmName(String firmName) {
        this.firmName = firmName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileNumber2() {
        return mobileNumber2;
    }

    public void setMobileNumber2(String mobileNumber2) {
        this.mobileNumber2 = mobileNumber2;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getShopImage() {
        return shopImage;
    }

    public void setShopImage(String shopImage) {
        this.shopImage = shopImage;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSatate() {
        return satate;
    }

    public void setSatate(String satate) {
        this.satate = satate;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLattitude() {
        return lattitude;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public String getLangitude() {
        return langitude;
    }

    public void setLangitude(String langitude) {
        this.langitude = langitude;
    }

    public Boolean getInterstCity() {
        return interstCity;
    }

    public void setInterstCity(Boolean interstCity) {
        this.interstCity = interstCity;
    }

    public Boolean getInterstState() {
        return interstState;
    }

    public void setInterstState(Boolean interstState) {
        this.interstState = interstState;
    }

    public Boolean getInterstCountry() {
        return interstCountry;
    }

    public void setInterstCountry(Boolean interstCountry) {
        this.interstCountry = interstCountry;
    }

    public String getRegistrationType() {
        return registrationType;
    }

    public void setRegistrationType(String registrationType) {
        this.registrationType = registrationType;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankBranchName() {
        return bankBranchName;
    }

    public void setBankBranchName(String bankBranchName) {
        this.bankBranchName = bankBranchName;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getiFSCCode() {
        return iFSCCode;
    }

    public void setiFSCCode(String iFSCCode) {
        this.iFSCCode = iFSCCode;
    }

    public String getBankCity() {
        return bankCity;
    }

    public void setBankCity(String bankCity) {
        this.bankCity = bankCity;
    }

    public Integer getSalesmanID() {
        return salesmanID;
    }

    public void setSalesmanID(Integer salesmanID) {
        this.salesmanID = salesmanID;
    }

    public Integer getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(Integer createdByID) {
        this.createdByID = createdByID;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(Integer isRegistered) {
        this.isRegistered = isRegistered;
    }

    public List<BusinessType> getBusinessTypeWithCust() {
        return businessTypeWithCust;
    }

    public void setBusinessTypeWithCust(List<BusinessType> businessTypeWithCust) {
        this.businessTypeWithCust = businessTypeWithCust;
    }

    public List<MainCategoryProduct> getCategoryTypeWithCust() {
        return categoryTypeWithCust;
    }

    public void setCategoryTypeWithCust(List<MainCategoryProduct> categoryTypeWithCust) {
        this.categoryTypeWithCust = categoryTypeWithCust;
    }

    public List<SubCategoryProduct> getSubCategoryTypeWithCust() {
        return subCategoryTypeWithCust;
    }

    public void setSubCategoryTypeWithCust(List<SubCategoryProduct> subCategoryTypeWithCust) {
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
    }

    public List<ChildCategoryProduct> getChildCategoryTypeWithCust() {
        return childCategoryTypeWithCust;
    }

    public void setChildCategoryTypeWithCust(List<ChildCategoryProduct> childCategoryTypeWithCust) {
        this.childCategoryTypeWithCust = childCategoryTypeWithCust;
    }

    @Override
    public String toString() {
        return "Register{" +
                "custID=" + custID +
                ", firmName='" + firmName + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", custName='" + custName + '\'' +
                ", emailID='" + emailID + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", mobileNumber2='" + mobileNumber2 + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", area='" + area + '\'' +
                ", city='" + city + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", satate='" + satate + '\'' +
                ", pincode='" + pincode + '\'' +
                ", lattitude='" + lattitude + '\'' +
                ", langitude='" + langitude + '\'' +
                ", interstCity=" + interstCity +
                ", interstState=" + interstState +
                ", interstCountry=" + interstCountry +
                ", registrationType='" + registrationType + '\'' +
                ", tinNumber='" + tinNumber + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", bankname='" + bankname + '\'' +
                ", bankBranchName='" + bankBranchName + '\'' +
                ", accountnumber='" + accountnumber + '\'' +
                ", iFSCCode='" + iFSCCode + '\'' +
                ", bankCity='" + bankCity + '\'' +
                ", salesmanID=" + salesmanID +
                ", createdByID=" + createdByID +
                ", createdDate='" + createdDate + '\'' +
                ", isRegistered=" + isRegistered +
                ", businessTypeWithCust=" + businessTypeWithCust +
                ", categoryTypeWithCust=" + categoryTypeWithCust +
                ", subCategoryTypeWithCust=" + subCategoryTypeWithCust +
                ", childCategoryTypeWithCust=" + childCategoryTypeWithCust +
                '}';
    }
}
