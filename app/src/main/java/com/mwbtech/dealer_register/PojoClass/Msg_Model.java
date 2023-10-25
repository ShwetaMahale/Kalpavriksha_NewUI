package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by wbtech5 on 3/6/2020.
 */

public class Msg_Model {

    @SerializedName("FirmName")
    private  String Name;

    @SerializedName("Requirements")
    private String Requirements;

    @SerializedName("VillageLocalityname")
    private String VillageLocalityname;

    @SerializedName("MobileNumber")
    private String MobileNumber;

    @SerializedName("BusinessDemand")
    private String BusinessDemand;


    @SerializedName("MessageList")
    private ArrayList<Messages> messagesArrayList;

    @SerializedName("QueryId")
    private int QueryId;

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("Image")
    private String Image;

    @SerializedName("Image2")
    private String Image2;

    @SerializedName("CreatedDate")
    private String CreatedDate;


    public String getVillageLocalityname() {
        return VillageLocalityname;
    }

    public void setVillageLocalityname(String villageLocalityname) {
        VillageLocalityname = villageLocalityname;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public String getBusinessDemand() {
        return BusinessDemand;
    }

    public void setBusinessDemand(String businessDemand) {
        BusinessDemand = businessDemand;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getRequirements() {
        return Requirements;
    }

    public void setRequirements(String requirements) {
        Requirements = requirements;
    }

    public ArrayList<Messages> getMessagesArrayList() {
        return messagesArrayList;
    }

    public void setMessagesArrayList(ArrayList<Messages> messagesArrayList) {
        this.messagesArrayList = messagesArrayList;
    }

    public int getQueryId() {
        return QueryId;
    }

    public void setQueryId(int queryId) {
        QueryId = queryId;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public void setImage(String image) {
        Image = image;
    }


    public void setImage2(String image) {
        Image2 = image;
    }   //RK_10/06/2020

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public int getCustID() {
        return CustID;
    }

    public String getImage() {
        return Image;
    }

    public String getImage2() {
        return Image2;
    }  //RK_10/06/2020

    public String getCreatedDate() {
        return CreatedDate;
    }

}
