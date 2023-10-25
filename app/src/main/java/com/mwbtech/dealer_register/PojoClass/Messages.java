package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by wbtech5 on 3/7/2020.
 */

public class Messages {

    public static final int SENDER_TYPE = 0;
    public static final int RECV_TYPE = 1;
    public static final int IMAGE_TYPE = 2;

    public int type;

    @SerializedName("ID")
    private int ID;

    @SerializedName("QueryId")
    private int QueryId;

    @SerializedName("CustID")
    @Expose
    private int custID;
    @SerializedName("CustomerName")
    @Expose
    private String customerName;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("IsDealer")
    @Expose
    private int isDealer;
    @SerializedName("IsCustomer")
    @Expose
    private int isCustomer;

    @SerializedName("CreatedBy")
    private int CreatedBy;

    @SerializedName("Image")
    private String Image;


    @SerializedName("Image2")
    private String Image2;

    @SerializedName("FileType")
    private String FileType;

    @SerializedName("FileName")
    private String FileName;

    String CreatedDate;


    public Messages(int type, String text, String img, String CreatedDate, int queryId, int id,String fileType,String fileName) {
        this.type = type;
        this.message = text;
        this.Image = img;
        this.CreatedDate = CreatedDate;
        this.QueryId = queryId;
        this.ID = id;
        this.FileType = fileType;
        this.FileName=fileName;
    }

    public Messages(int type, String text, String img, String img2, String CreatedDate) {
        this.type = type;
        this.message = text;
        this.Image = img;
        this.Image2 = img2;
        this.CreatedDate = CreatedDate;
    }


    public Messages(int type, String text, String img, String img2, String CreatedDate,String fileType) {
        this.type = type;
        this.message = text;
        this.Image = img;
        this.Image2 = img2;
        this.CreatedDate = CreatedDate;
        this.FileType = fileType;
    }

    public Messages(int queryID, Integer custID, String message, Integer isDealer, Integer isCustomer, String image, Integer createdBy) {
        QueryId = queryID;
        this.custID = custID;
        this.message = message;
        this.isDealer = isDealer;
        this.isCustomer = isCustomer;
        this.Image = image;
        this.CreatedBy = createdBy;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileType() {
        return FileType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public int getQueryId() {
        return QueryId;
    }

    public void setQueryId(int queryId) {
        QueryId = queryId;
    }

    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getIsDealer() {
        return isDealer;
    }

    public void setIsDealer(Integer isDealer) {
        this.isDealer = isDealer;
    }

    public Integer getIsCustomer() {
        return isCustomer;
    }

    public void setIsCustomer(Integer isCustomer) {
        this.isCustomer = isCustomer;
    }

    public String getImage2() {
        return Image2;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "custID=" + custID +
                ", message='" + message + '\'' +
                ", Image='" + Image + '\'' +
                ", Image2='" + Image2 + '\'' +
                '}';
    }
}
