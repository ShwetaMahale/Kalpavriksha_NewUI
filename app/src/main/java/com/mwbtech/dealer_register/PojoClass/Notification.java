package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notification implements Serializable {

    @SerializedName("ID")
    private int ID;

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("PushNotification")
    private String PushNotification;


    @SerializedName("NotificationDateStr")
    private String NotificationDateStr;

    @SerializedName("CategoryName")
    private String CategoryName;

    @SerializedName("ImageURL")
    private String ImageURL;

    @SerializedName("Title")
    private String title;

    @SerializedName("QueryID")
    private int QueryID;

    @SerializedName("NotificationType")
    private String NotificationType;

    @SerializedName("CreatedBy")
    private int CreatedBy;

    @SerializedName("NotificationDate")
    private String createdDate;

    @SerializedName("customerDetailsQuestionDTO")
    private CustomerDetailsQuestionDTO customerDetailsQuestionDTO;

    public Notification(int ID, int custID, String pushNotification) {
        this.ID = ID;
        CustID = custID;
        PushNotification = pushNotification;
    }


    public Notification(int ID, int custID, String pushNotification, String notificationDateStr, String title, String categoryName, String imageURL, int queryID, CustomerDetailsQuestionDTO customerDetailsQuestionDTO,String notificationType,int createdBy,String createddate) {
        this.ID = ID;
        CustID = custID;
        PushNotification = pushNotification;
        NotificationDateStr = notificationDateStr;
        this.title = title;
        CategoryName = categoryName;
        ImageURL = imageURL;
        QueryID = queryID;
        this.customerDetailsQuestionDTO = customerDetailsQuestionDTO;
        NotificationType = notificationType;
        CreatedBy = createdBy;
        createdDate=createddate;

    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public String getPushNotification() {
        return PushNotification;
    }

    public void setPushNotification(String pushNotification) {
        PushNotification = pushNotification;
    }

    public String getNotificationDateStr() {
        return NotificationDateStr;
    }

    public void setNotificationDateStr(String notificationDateStr) {
        NotificationDateStr = notificationDateStr;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public int getQueryID() {
        return QueryID;
    }

    public void setQueryID(int queryID) {
        QueryID = queryID;
    }

    public CustomerDetailsQuestionDTO getCustomerDetailsQuestionDTO() {
        return customerDetailsQuestionDTO;
    }

    public void setCustomerDetailsQuestionDTO(CustomerDetailsQuestionDTO customerDetailsQuestionDTO) {
        this.customerDetailsQuestionDTO = customerDetailsQuestionDTO;
    }

    public String getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(String notificationType) {
        NotificationType = notificationType;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
