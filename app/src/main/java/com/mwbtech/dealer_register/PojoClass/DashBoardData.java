package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class DashBoardData {

    @SerializedName("SentEnquiryMessagesCount")
    private int SentEnquiryMessagesCount;

    @SerializedName("FavouriteEnquiryMessagesCount")
    private int FavouriteEnquiryMessagesCount;

    @SerializedName("ReceivedEnquiryMessagesCount")
    private int ReceivedEnquiryMessagesCount;

    @SerializedName("NotificationsCount")
    private int NotificationsCount;

    @SerializedName("IsRegistered")
    private int isRegistered;

    public int getSentEnquiryMessagesCount() {
        return SentEnquiryMessagesCount;
    }

    public void setSentEnquiryMessagesCount(int sentEnquiryMessagesCount) {
        SentEnquiryMessagesCount = sentEnquiryMessagesCount;
    }

    public int getFavouriteEnquiryMessagesCount() {
        return FavouriteEnquiryMessagesCount;
    }

    public void setFavouriteEnquiryMessagesCount(int favouriteEnquiryMessagesCount) {
        FavouriteEnquiryMessagesCount = favouriteEnquiryMessagesCount;
    }

    public int getReceivedEnquiryMessagesCount() {
        return ReceivedEnquiryMessagesCount;
    }

    public void setReceivedEnquiryMessagesCount(int receivedEnquiryMessagesCount) {
        ReceivedEnquiryMessagesCount = receivedEnquiryMessagesCount;
    }

    public int getNotificationsCount() {
        return NotificationsCount;
    }

    public void setNotificationsCount(int notificationsCount) {
        NotificationsCount = notificationsCount;
    }

    public int getIsRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(int isRegistered) {
        this.isRegistered = isRegistered;
    }
}
