package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class PostPayment {
    @SerializedName("OrderID")
    public String orderID;
    @SerializedName("OrderAmount")
    public double orderAmount;
    @SerializedName("TxnStatusMessage")
    public String txnStatusMessage;
    @SerializedName("TxnStatus")
    public String txnStatus;
    @SerializedName("DisplayMessage")
    public String displayMessage;
    @SerializedName("txnPaymentMode")
    public String txnPaymentMode;
    @SerializedName("txnSignature")
    public String txnSignature;
    @SerializedName("txnType")
    public String txnType;
    @SerializedName("txnTime")
    public String txnTime;
    @SerializedName("txnReferenceID")
    public String txnReferenceID;
    @SerializedName("CustID")
    public Integer custID;
    @SerializedName("AdvertisementMainID")
    public Integer advertisementMainID;

    public PostPayment(String orderID, double orderAmount, String txnStatusMessage, String txnStatus, String txnPaymentMode, String txnSignature, String txnType, String txnTime, String txnReferenceID,Integer custID,Integer advertisementMainID ) {
        this.orderID = orderID;
        this.orderAmount = orderAmount;
        this.txnStatusMessage = txnStatusMessage;
        this.txnStatus = txnStatus;
        this.txnPaymentMode = txnPaymentMode;
        this.txnSignature = txnSignature;
        this.txnType = txnType;
        this.txnTime = txnTime;
        this.txnReferenceID = txnReferenceID;
        this.custID=custID;
        this.advertisementMainID=advertisementMainID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTxnStatusMessage() {
        return txnStatusMessage;
    }

    public void setTxnStatusMessage(String txnStatusMessage) {
        this.txnStatusMessage = txnStatusMessage;
    }

    public String getTxnStatus() {
        return txnStatus;
    }

    public void setTxnStatus(String txnStatus) {
        this.txnStatus = txnStatus;
    }

    public String getDisplayMessage() {
        return displayMessage;
    }

    public void setDisplayMessage(String displayMessage) {
        this.displayMessage = displayMessage;
    }

    public String getTxnPaymentMode() {
        return txnPaymentMode;
    }

    public void setTxnPaymentMode(String txnPaymentMode) {
        this.txnPaymentMode = txnPaymentMode;
    }

    public String getTxnSignature() {
        return txnSignature;
    }

    public void setTxnSignature(String txnSignature) {
        this.txnSignature = txnSignature;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnTime() {
        return txnTime;
    }

    public void setTxnTime(String txnTime) {
        this.txnTime = txnTime;
    }

    public String getTxnReferenceID() {
        return txnReferenceID;
    }

    public void setTxnReferenceID(String txnReferenceID) {
        this.txnReferenceID = txnReferenceID;
    }
}
