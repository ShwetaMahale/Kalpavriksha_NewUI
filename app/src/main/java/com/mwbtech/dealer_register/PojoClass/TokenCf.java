package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class TokenCf {

    @SerializedName("status")
    private String status;

    @SerializedName("message")
    private String message;

    public TokenCf(String status, String message, String cftoken) {
        this.status = status;
        this.message = message;
        this.cftoken = cftoken;
    }

    @SerializedName("cftoken")
    private String cftoken;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCftoken() {
        return cftoken;
    }

    public void setCftoken(String cftoken) {
        this.cftoken = cftoken;
    }
}
