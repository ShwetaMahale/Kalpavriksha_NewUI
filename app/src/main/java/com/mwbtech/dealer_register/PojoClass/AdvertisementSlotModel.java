package com.mwbtech.dealer_register.PojoClass;

import com.google.gson.annotations.SerializedName;

public class AdvertisementSlotModel {

    @SerializedName("ID")
    private int AdSlotId;

    @SerializedName("TimeSlotName")
    private String TimeSlotName;

    @SerializedName("Description")
    private String Description;

    @SerializedName("TimeSlotMatrix")
    private double TimeSlotMatrix;

    private boolean isSelected=false;

    public AdvertisementSlotModel(int adSlotId, String timeSlotName) {
        AdSlotId = adSlotId;
        TimeSlotName = timeSlotName;
    }

    public AdvertisementSlotModel(int adSlotId, String timeSlotName, double timeSlotMatrix) {
        AdSlotId = adSlotId;
        TimeSlotName = timeSlotName;
        TimeSlotMatrix = timeSlotMatrix;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getAdSlotId() {
        return AdSlotId;
    }

    public void setAdSlotId(int adSlotId) {
        AdSlotId = adSlotId;
    }

    public String getTimeSlotName() {
        return TimeSlotName;
    }

    public void setTimeSlotName(String timeSlotName) {
        TimeSlotName = timeSlotName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return  TimeSlotName;
    }


    public double getTimeSlotMatrix() {
        return TimeSlotMatrix;
    }

    public void setTimeSlotMatrix(double timeSlotMatrix) {
        TimeSlotMatrix = timeSlotMatrix;
    }
}
