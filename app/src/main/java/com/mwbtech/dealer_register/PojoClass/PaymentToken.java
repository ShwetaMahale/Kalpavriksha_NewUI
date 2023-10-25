package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentToken {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("orderAmount")
    @Expose
    private Integer orderAmount;
    @SerializedName("orderCurrency")
    @Expose
    private String orderCurrency;

    public PaymentToken(String orderId, Integer orderAmount, String orderCurrency) {
        this.orderId = orderId;
        this.orderAmount = orderAmount;
        this.orderCurrency = orderCurrency;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(Integer orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderCurrency() {
        return orderCurrency;
    }

    public void setOrderCurrency(String orderCurrency) {
        this.orderCurrency = orderCurrency;
    }
}
