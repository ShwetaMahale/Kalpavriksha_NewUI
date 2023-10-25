package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class DeleteInbox {

    @SerializedName("CustID")
    private final int CustID;

    @SerializedName("QueryId")
    private final int QueryId;

    @SerializedName("ReceiverID")
    private int ReceiverID;

    @SerializedName("ID")
    private int ID;

    public DeleteInbox(int custID, int queryId, int receiverID) {
        CustID = custID;
        QueryId = queryId;
        ReceiverID = receiverID;
    }

    public DeleteInbox(int custID, int queryId, int receiverID,int id) {
        CustID = custID;
        QueryId = queryId;
        ReceiverID = receiverID;
        ID=id;
    }

    public DeleteInbox(int custID, int queryId) {
        CustID = custID;
        QueryId = queryId;
    }

    public int getCustID() {
        return CustID;
    }

    public int getQueryId() {
        return QueryId;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    @Override
    public String toString() {
        return "DeleteInbox{" +
                "CustID=" + CustID +
                ", QueryId=" + QueryId +
                ", ReceiverID=" + ReceiverID +
                '}';
    }
}
