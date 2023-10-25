package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class DealerRegister implements Serializable {
    @SerializedName("ID")
    @Expose
//    private Integer userID;
//    @SerializedName("CustID")
//    @Expose
    private Integer custID;
    @SerializedName("FirmName")
    @Expose
    private String firmName;
    @SerializedName("Password")
    @Expose
    private String password;
    @SerializedName("UserType")
    @Expose
    private String userType = "0";
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
    private String billingAddress = "";
    @SerializedName("Area")
    @Expose
    private String area = "";
    @SerializedName(value = "city", alternate = {"City"})
    @Expose
    private City city;
    @SerializedName("CityCode")
    @Expose
    private String cityCode = "";
    @SerializedName(value = "state", alternate = {"State"})
    @Expose
    private State state;

    //ads
    @SerializedName("CityId")
    @Expose
    private int CityID;

    @SerializedName("VillageLocalityName")
    @Expose
    private String villageLocalityName;

    @SerializedName("StateId")
    @Expose
    private int StateID;

    @SerializedName("StateName")
    @Expose
    private String StateName;

    @SerializedName("ChildCategoryId")
    @Expose
    private int ChildCategoryId;

    @SerializedName("ChildCategoryName")
    @Expose
    private String ChildCategoryName;

    @SerializedName("AdUserID")
    @Expose
    private int AdUserID;

    @SerializedName("AdFirmName")
    @Expose
    private String AdFirmName;

    @SerializedName("BusinessTypes")
    @Expose
    private List<BusinessType> businessTypeIDList;
    //
    @SerializedName("IsActive")
    private Boolean isActive;

    @SerializedName("district")
    private District district;

    @SerializedName("Pincode")
    @Expose
    private String pincode = "";
    @SerializedName("Lattitude")
    @Expose
    private String lattitude = "";
    @SerializedName("Langitude")
    @Expose
    private String langitude = "";
    @SerializedName("InterstCity")
    @Expose
    private Boolean interstCity;
    @SerializedName("InterstState")
    @Expose
    private Boolean interstState;
    @SerializedName("InterstCountry")
    @Expose
    private Boolean interstCountry;
    @SerializedName("InterstDistrict")
    @Expose
    private Boolean interstDistrict;
    @SerializedName("RegistrationType")
    @Expose
    private String registrationType;
    @SerializedName("TinNumber")
    @Expose
    private String tinNumber = "";
    @SerializedName("PanNumber")
    @Expose
    private String panNumber = "";
    @SerializedName("Bankname")
    @Expose
    private String bankname = "";
    @SerializedName("BankBranchName")
    @Expose
    private String bankBranchName = "";
    @SerializedName("Accountnumber")
    @Expose
    private String accountnumber = "";
    @SerializedName("IFSCCode")
    @Expose
    private String iFSCCode = "";
    @SerializedName("BankCity")
    @Expose
    private String bankCity = "";
    @SerializedName("SalesmanID")
    @Expose
    private Integer salesmanID;

    @SerializedName("IsProfessional")
    @Expose
    private boolean isProfessional = false;

    @SerializedName("IsBGSMember")
    @Expose
    private boolean IsBGSMember;

    @SerializedName("UserImage")
    @Expose
    private String UserImage;

    @SerializedName("CreatedByID")
    @Expose
    private Integer createdByID;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("IsRegistered")
    @Expose
    private Integer isRegistered=1;
    @SerializedName("BusinessTypeWithCust")
    @Expose
    private List<BusinessType> businessTypeWithCust = null;
    @SerializedName("CategoryTypeWithCust")
    @Expose
    private List<MainCategoryProduct> categoryTypeWithCust = null;
    @SerializedName("SubCategoryTypeWithCust")
    @Expose
    private List<SubCategoryProduct> subCategoryTypeWithCust = null;
    /*@SerializedName("ChildCategoryTypeWithCust")
    @Expose
    private List<ChildCategoryProduct> childCategoryTypeWithCust = null;*/

    @SerializedName("AdditionalPersonName")
    @Expose
    private String AdditionalPersonName = "";

    @SerializedName("ProductRegistered")
    @Expose
    private boolean ProductRegistered;

    private boolean IsOTPVerified;
    private boolean IsTAndCAgreed;
    private String TAndCVersion;

    private String OTPStatus;

    public boolean isChecked;

    @SerializedName("FullScreenAdURL")
    private String FullScreenAdURL;

    // @SerializedName("UserImage")
    public File UserImageFile;

    @SerializedName("IsWelcomeMsg")
    @Expose
    private String IsWelcomeMsg="";

    @SerializedName("DeviceID")
    private String DeviceID;

    @SerializedName("BusinessDemandID")
    private int BusinessDemandID;

    @SerializedName("BusinessDemandWithCust")
    @Expose
    private List<BusinessDemand> BusinessDemandWithCust = null;


    public DealerRegister(String deviceID) {
        DeviceID = deviceID;
    }

    public DealerRegister(String firmName, String password, String userType, String mobileNumber) {
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        this.mobileNumber = mobileNumber;
    }

    public DealerRegister(String firmName, String password, String userType, String mobileNumber, boolean isTAndCAgreed, String TAndCVersion, String deviceID) {
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        this.mobileNumber = mobileNumber;
        IsTAndCAgreed = isTAndCAgreed;
        this.TAndCVersion = TAndCVersion;
        DeviceID = deviceID;
    }

    public DealerRegister() {
    }

    public DealerRegister(int custID, String firmName, String mobileNumber) {
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.mobileNumber = mobileNumber;
    }

    public DealerRegister(int custID, String firmName, String mobileNumber, boolean isChecked) {
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.mobileNumber = mobileNumber;
        this.isChecked = isChecked;
    }

    public DealerRegister(List<BusinessDemand> businessDemandWithCust) {
        BusinessDemandWithCust = businessDemandWithCust;
    }

    public DealerRegister(int custID, String firmName, String password, String userType, List<SubCategoryProduct> subCategoryTypeWithCust, List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber, String additionalPersonName,
                          String mobileNumber2, String telephoneNumber, String shopImage, boolean IsBGSMember, File UserImageFile, int businessDemandID) {  //, String UserImage
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        //this.childCategoryTypeWithCust = childCategoryTypeWithCust;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.categoryTypeWithCust = categoryTypeWithCust;
        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.IsBGSMember = IsBGSMember;
        //this.UserImage = UserImage;
        this.UserImageFile = UserImageFile;
        this.BusinessDemandID = businessDemandID;

    }

    public DealerRegister(int custID, String firmName, String password, String userType, List<SubCategoryProduct> subCategoryTypeWithCust, List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber, String additionalPersonName,
                          String mobileNumber2, String telephoneNumber, String shopImage, boolean IsBGSMember, boolean isProfessional, File UserImageFile, int businessDemandID) {  //, String UserImage
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        //this.childCategoryTypeWithCust = childCategoryTypeWithCust;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.categoryTypeWithCust = categoryTypeWithCust;
        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.IsBGSMember = IsBGSMember;
        this.isProfessional = isProfessional;
        //this.UserImage = UserImage;
        this.UserImageFile = UserImageFile;
        this.BusinessDemandID = businessDemandID;

    }


    public DealerRegister(int custID, String firmName, String password, String userType, List<SubCategoryProduct> subCategoryTypeWithCust, List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber, String additionalPersonName,
                          String mobileNumber2, String telephoneNumber, String shopImage, boolean IsBGSMember, File UserImageFile, int businessDemandID, List<BusinessDemand> businessDemandWithCust) {  //, String UserImage
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        //this.childCategoryTypeWithCust = childCategoryTypeWithCust;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.categoryTypeWithCust = categoryTypeWithCust;
        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.IsBGSMember = IsBGSMember;
        //this.UserImage = UserImage;
        this.UserImageFile = UserImageFile;
        this.BusinessDemandID = businessDemandID;
        BusinessDemandWithCust = businessDemandWithCust;
    }


    public DealerRegister(int custID, String firmName, String password, String userType, List<SubCategoryProduct> subCategoryTypeWithCust, List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber, String additionalPersonName,
                          String mobileNumber2, String telephoneNumber, String shopImage, boolean IsBGSMember, boolean isProfessional, File UserImageFile, int businessDemandID, List<BusinessDemand> businessDemandWithCust) {  //, String UserImage
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        //this.childCategoryTypeWithCust = childCategoryTypeWithCust;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.categoryTypeWithCust = categoryTypeWithCust;
        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.IsBGSMember = IsBGSMember;
        this.isProfessional = isProfessional;
        //this.UserImage = UserImage;
        this.UserImageFile = UserImageFile;
        this.BusinessDemandID = businessDemandID;
        BusinessDemandWithCust = businessDemandWithCust;
    }

    public DealerRegister(Boolean interstDistrict) {
        this.interstDistrict = interstDistrict;
    }

    public DealerRegister(int custID, String firmName, String password, String userType, List<SubCategoryProduct> subCategoryTypeWithCust,
                          List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber,
                          String additionalPersonName, String mobileNumber2, String telephoneNumber, String shopImage, String billingAddress,
                          String area, City city, String cityCode, State state, String pincode,
                          String lattitude, String langitude, Boolean interstCity, Boolean interstState, Boolean isProfessional,
                          Boolean interstCountry, String registrationType, String tinNumber, String panNumber, int businessDemandID, List<BusinessDemand> businessDemandWithCust, Boolean interstDistrict) {
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;
        this.subCategoryTypeWithCust = subCategoryTypeWithCust;
        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = state;
        this.pincode = pincode;
        this.lattitude = lattitude;
        this.langitude = langitude;
        this.interstCity = interstCity;
        this.interstState = interstState;
        this.isProfessional = isProfessional;
        this.interstCountry = interstCountry;
        this.registrationType = registrationType;
        this.tinNumber = tinNumber;
        this.panNumber = panNumber;
        this.BusinessDemandID = businessDemandID;
        BusinessDemandWithCust = businessDemandWithCust;
        this.interstDistrict = interstDistrict;
    }

    public DealerRegister(Integer custID, String firmName, String password, String userType,
                          List<SubCategoryProduct> subCategoryTypeWithCust,
                          List<BusinessType> businessTypeWithCust, String custName, String emailID, String mobileNumber,
                          String additionalPersonName, String mobileNumber2, String telephoneNumber, String shopImage, String billingAddress,
                          String area, City city, String cityCode, State satate,
                          String pincode, String lattitude, String langitude,
                          Boolean interstCity, Boolean interstState, Boolean interstCountry,
                          String registrationType, String tinNumber, String panNumber,
                          String bankname, String bankBranchName, String accountnumber,
                          String iFSCCode, String bankCity, Integer salesmanID,
                          Integer createdByID, Integer isRegistered, String createdDate,
                          boolean IsBGSMember, boolean isProfessional, File UserImageFile, int businessDemandID, List<BusinessDemand> businessDemandWithCust, Boolean interstDistrict) {  //String UserImage
//        this.userID = custID;
        this.custID = custID;
        this.firmName = firmName;
        this.password = password;
        this.userType = userType;

        this.subCategoryTypeWithCust = subCategoryTypeWithCust;

        this.businessTypeWithCust = businessTypeWithCust;
        this.custName = custName;
        this.emailID = emailID;
        this.mobileNumber = mobileNumber;
        this.AdditionalPersonName = additionalPersonName;
        this.mobileNumber2 = mobileNumber2;
        this.telephoneNumber = telephoneNumber;
        this.shopImage = shopImage;
        this.billingAddress = billingAddress;
        this.area = area;
        this.city = city;
        this.cityCode = cityCode;
        this.state = satate;

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
        this.IsBGSMember = IsBGSMember;
        //this.UserImage = UserImage;
        this.isProfessional = isProfessional;
        this.UserImageFile = UserImageFile;
        this.BusinessDemandID = businessDemandID;
        BusinessDemandWithCust = businessDemandWithCust;
        this.interstDistrict = interstDistrict;
    }

    public boolean isOTPVerified() {
        return IsOTPVerified;
    }

    public void setOTPVerified(boolean OTPVerified) {
        IsOTPVerified = OTPVerified;
    }

    public String getOTPStatus() {
        return OTPStatus;
    }

    public void setOTPStatus(String OTPStatus) {
        this.OTPStatus = OTPStatus;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public State getState() {
        return state;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public Integer getUserID() {
        return custID;
    }

    public void setUserID(Integer userID) {
        this.custID = userID;
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

    public boolean getIsProfessional() {
        return isProfessional;
    }

    public void setIsProfessional(boolean professional) {
        isProfessional = professional;
    }

    public String getUserType() {
        if (userType == null)
            return "1";
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


    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdditionalPersonName() {
        return AdditionalPersonName;
    }

    public void setAdditionalPersonName(String additionalPersonName) {
        AdditionalPersonName = additionalPersonName;
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

    public boolean isBGSMember() {
        return IsBGSMember;
    }

    public void setBGSMember(boolean BGSMember) {
        IsBGSMember = BGSMember;
    }

    public String isUserImage() {
        return UserImage;
    }

    public void setUserImage(String userImage) {
        UserImage = userImage;
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

    public File getUserImageFile() {
        return UserImageFile;
    }

    public void setUserImageFile(File userImageFile) {
        UserImageFile = userImageFile;
    }

    public String getUserImage() {
        return UserImage;
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
        if(isRegistered==null)
            return  1;
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

    public List<BusinessDemand> getBusinessDemandWithCust() {
        return BusinessDemandWithCust;
    }

    public void setBusinessDemandWithCust(List<BusinessDemand> businessDemandWithCust) {
        BusinessDemandWithCust = businessDemandWithCust;
    }
/*public List<ChildCategoryProduct> getChildCategoryTypeWithCust() {
        return childCategoryTypeWithCust;
    }

    public void setChildCategoryTypeWithCust(List<ChildCategoryProduct> childCategoryTypeWithCust) {
        this.childCategoryTypeWithCust = childCategoryTypeWithCust;
    }*/


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFullScreenAdURL() {
        return FullScreenAdURL;
    }

    public int getCityID() {
        return CityID;
    }

    public void setCityID(int cityID) {
        CityID = cityID;
    }

    public String getVillageLocalityName() {
        return villageLocalityName;
    }

    public void setVillageLocalityName(String villageLocalityName) {
        this.villageLocalityName = villageLocalityName;
    }

    public int getStateID() {
        return StateID;
    }

    public void setStateID(int stateID) {
        StateID = stateID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public int getChildCategoryId() {
        return ChildCategoryId;
    }

    public void setChildCategoryId(int childCategoryId) {
        ChildCategoryId = childCategoryId;
    }

    public String getChildCategoryName() {
        return ChildCategoryName;
    }

    public void setChildCategoryName(String childCategoryName) {
        ChildCategoryName = childCategoryName;
    }

    public int getAdUserID() {
        return AdUserID;
    }

    public void setAdUserID(int adUserID) {
        AdUserID = adUserID;
    }

    public String getAdFirmName() {
        return AdFirmName;
    }

    public void setAdFirmName(String adFirmName) {
        AdFirmName = adFirmName;
    }

    public void setFullScreenAdURL(String fullScreenAdURL) {
        FullScreenAdURL = fullScreenAdURL;
    }

    public List<BusinessType> getBusinessTypeIDList() {
        return businessTypeIDList;
    }

    public void setBusinessTypeIDList(List<BusinessType> businessTypeIDList) {
        this.businessTypeIDList = businessTypeIDList;
    }

    public boolean isTAndCAgreed() {
        return IsTAndCAgreed;
    }

    public void setTAndCAgreed(boolean TAndCAgreed) {
        IsTAndCAgreed = TAndCAgreed;
    }

    public String getTAndCVersion() {
        return TAndCVersion;
    }

    public void setTAndCVersion(String TAndCVersion) {
        this.TAndCVersion = TAndCVersion;
    }

    public String getIsWelcomeMsg() {
        return IsWelcomeMsg;
    }

    public void setIsWelcomeMsg(String isWelcomeMsg) {
        IsWelcomeMsg = isWelcomeMsg;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public int getBusinessDemandID() {
        return BusinessDemandID;
    }

    public void setBusinessDemandID(int businessDemandID) {
        BusinessDemandID = businessDemandID;
    }

    public Boolean getInterstDistrict() {
        return interstDistrict;
    }

    public void setInterstDistrict(Boolean interstDistrict) {
        this.interstDistrict = interstDistrict;
    }

    public boolean isProductRegistered() {
        return ProductRegistered;
    }

    public void setProductRegistered(boolean productRegistered) {
        ProductRegistered = productRegistered;
    }

    @Override
    public String toString() {
        return "DealerRegister{" +
//                "userID=" + userID +
                "custID=" + custID +
                ", firmName='" + firmName + '\'' +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", custName='" + custName + '\'' +
                ", emailID='" + emailID + '\'' +
                ",FullscreenAd=" + FullScreenAdURL + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", mobileNumber2='" + mobileNumber2 + '\'' +
                ", telephoneNumber='" + telephoneNumber + '\'' +
                ", shopImage='" + shopImage + '\'' +
                ", UserImageFile='" + UserImageFile + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", area='" + area + '\'' +
                ", city=" + city +
                ", cityCode='" + cityCode + '\'' +
                ", state=" + state +
                ", district=" + district +
                ", pincode='" + pincode + '\'' +
                ", lattitude='" + lattitude + '\'' +
                ", langitude='" + langitude + '\'' +
                ", interstCity=" + interstCity +
                ", interstDistrict=" + interstDistrict +
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
                ", isProfessional=" + isProfessional +
                ", businessTypeWithCust=" + businessTypeWithCust +
                ", categoryTypeWithCust=" + categoryTypeWithCust +
                ", subCategoryTypeWithCust=" + subCategoryTypeWithCust +
                ", BusinessDemandWithCust=" + BusinessDemandWithCust +
                ", AdditionalPersonName='" + AdditionalPersonName + '\'' +
                ", IsOTPVerified=" + IsOTPVerified +
                ", OTPStatus='" + OTPStatus + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
