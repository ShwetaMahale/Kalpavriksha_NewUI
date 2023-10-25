package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class ReadUnreadModel {
    @SerializedName("QueryId")
    private int queryId;

    @SerializedName("CustID")
    private int CustID;

    @SerializedName("ReceiverID")
    private int ReceiverID;

    @SerializedName("SenderRead")
    private int SenderRead;

    @SerializedName("IsRead")
    private int IsRead;

    public ReadUnreadModel(int queryId, int custID, int receiverID,  int isRead) {
        this.queryId = queryId;
        CustID = custID;
        ReceiverID = receiverID;
        IsRead = isRead;
    }

    public int getQueryId() {
        return queryId;
    }

    public void setQueryId(int queryId) {
        this.queryId = queryId;
    }

    public int getCustID() {
        return CustID;
    }

    public void setCustID(int custID) {
        CustID = custID;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(int receiverID) {
        ReceiverID = receiverID;
    }

    public int getSenderRead() {
        return SenderRead;
    }

    public void setSenderRead(int senderRead) {
        SenderRead = senderRead;
    }

    public int getIsRead() {
        return IsRead;
    }

    public void setIsRead(int isRead) {
        IsRead = isRead;
    }
}
