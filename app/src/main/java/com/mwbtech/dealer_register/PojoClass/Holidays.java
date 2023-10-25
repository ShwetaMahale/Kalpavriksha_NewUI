package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Holidays implements Serializable {
    @SerializedName("Value")
    private int Value;

    public int getValue() {
        return Value;
    }

    public void setValue(int value) {
        Value = value;
    }
}
