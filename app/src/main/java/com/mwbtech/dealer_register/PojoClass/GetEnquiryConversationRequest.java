package com.mwbtech.dealer_register.PojoClass;

import android.os.Parcel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetEnquiryConversationRequest implements Serializable {

    @SerializedName("QueryID")
    private int QueryID;

    @SerializedName("ReceiverID")
    private int ReceiverID;

    @SerializedName("CustID")
    @Expose
    private Integer custId;

    @SerializedName("IsFavorite")
    @Expose
    private Integer isFavorite;

    @SerializedName("SelectedCities")
    @Expose
    private String selectedCities;

    public GetEnquiryConversationRequest(int queryID, int receiverID, Integer custId, Integer isFavorite, String selectedCities) {
        QueryID = queryID;
        ReceiverID = receiverID;
        this.custId = custId;
        this.isFavorite = isFavorite;
        this.selectedCities = selectedCities;
    }

    public int getQueryID() {
        return QueryID;
    }

    public void setQueryID(int queryID) {
        QueryID = queryID;
    }

    public int getReceiverID() {
        return ReceiverID;
    }

    public void setReceiverID(int receiverID) {
        ReceiverID = receiverID;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(Integer isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getSelectedCities() {
        return selectedCities;
    }

    public void setSelectedCities(String selectedCities) {
        this.selectedCities = selectedCities;
    }
}
